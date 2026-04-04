package com.potatodevs.cropsamarica.repositories.survey
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.potatodevs.cropsamarica.ai.GENERATE_RECOMMENDATION
import com.potatodevs.cropsamarica.ai.SURVEY_GENERATION
import com.potatodevs.cropsamarica.ai.converters.asFertilizerTasks
import com.potatodevs.cropsamarica.ai.converters.asRecommendationToTasks
import com.potatodevs.cropsamarica.ai.converters.asSurvey
import com.potatodevs.cropsamarica.datastore.LocaleManager
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.survey.Survey
import com.potatodevs.cropsamarica.models.survey.SurveyWithRiceType
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.repositories.file.FileRepository
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import com.potatodevs.cropsamarica.repositories.weather.WeatherRepository
import com.potatodevs.cropsamarica.ui.main.home.subscreens.survey.QuestionWithAnswers
import com.potatodevs.cropsamarica.ui.utils.getRiceStage
import com.potatodevs.cropsamarica.utils.PHILRICE_URL
import com.potatodevs.cropsamarica.utils.toBitmap
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.jsonArray

import java.util.Date

class SurveyRepositoryImpl
    @Inject constructor(
        private val firestore: FirebaseFirestore,
        private val riceFieldRepository: RiceFieldRepository,
        private val model : GenerativeModel,
        private val weatherRepository: WeatherRepository,
        private val localeManager: LocaleManager,
        private val  context : android.content.Context,
        private val fileRepository : FileRepository
    )
    : SurveyRepository {
    override suspend fun generateSurvey(id: String): Result<Survey> {
        Log.d(TAG, "Starting survey generation for Field ID: $id")

        return try {
            val field = riceFieldRepository.getById(id).getOrThrow()
            val languageCode = localeManager.getSavedLanguageCode().first().lowercase()
            val text = """
                The farmer is currently growing rice in the '${field.riceField?.stage}' stage. 
                The variety is ${field.riceType?.name}. 
                Based on this context, generate a targeted diagnostic survey to determine 
                if they are ready for the next crop stage or if intervention is needed.
                
                 ### KNOWLEDGE SOURCE (VERY IMPORTANT)
                - You have access to this trusted agricultural source: $PHILRICE_URL
                - Prefer guidance, practices, and recommendations aligned with PhilRice standards when applicable.
                - Use this source especially when identifying diseases, pest management, and fertilizer practices.
        """.trimIndent()

            Log.d(TAG, "Prompting AI with stage: ${field.riceField?.stage} and variety: ${field.riceType?.name}")

            val prompt = content { text(text) }
            val response = model.generateContent(prompt)

            // Log if the AI returned a text response instead of a function call
            response.text?.let {
                Log.w(TAG, "AI returned plain text instead of function call: $it")
            }

            val functionCall = response.functionCalls.find { it.name == SURVEY_GENERATION }

            if (functionCall != null) {
                Log.d(TAG, "Function call detected: ${functionCall.name} with args: ${functionCall.args}")

                val survey = functionCall.args.asSurvey(crop = field) // Ensure your asSurvey fits your new logic

                if (survey != null) {
                    Log.i(TAG, "Successfully parsed survey with ${survey.questions.size} questions.")
                    Result.success(survey)
                } else {
                    Log.e(TAG, "Mapping failed: asSurvey() returned null for args: ${functionCall.args}")
                    Result.failure(Exception("Failed to map AI response to Survey object."))
                }
            } else {
                Log.e(TAG, "No function call found in AI response.")
                Result.failure(Exception("AI failed to generate a valid survey structure."))
            }

        } catch (e: Exception) {
            Log.e(TAG, "Exception during survey generation for ID $id", e)
            Result.failure(e)
        }
    }


    /***
     * This function should Generate AI recommendations for the next stage of the crop.
     * It takes the following parameters:
     * - survey: A list of QuestionWithAnswers representing the survey questions and answers.
     * - crop: A RiceFieldWithRiceType object representing the field and its associated rice type.
     * - images: A list of Uri objects representing the images captured during the survey (optional).
     * It returns a Result object containing either a list of Task objects or an error.
     */
    override suspend fun generateRecommendationForNextStage(
        survey: List<QuestionWithAnswers>,
        crop: RiceFieldWithRiceType,
        images: List<Uri>
    ): Result<List<Task>> {
        Log.d(TAG, "Generating recommendations for: ${crop.riceField?.name}")

        return try {
            val riceField = crop.riceField ?: return Result.failure(Exception("Rice field is null"))
            val nextStage = Date(riceField.plantedDate).getRiceStage()
            val languageCode = localeManager.getSavedLanguageCode().first().lowercase()

            val mdFile = "pest_and_diseases.md"
            val pestAndDiseaseContext = fileRepository.extractMD(mdFile).getOrElse {
                return Result.failure(Exception("Failed to load markdown context: ${it.message}", it))
            }

            val surveyContext = survey.joinToString("\n") { qa ->
                "Q: ${qa.question.text}\nA: ${qa.answer}"
            }

            val prompt = """
            ## ROLE
            You are a rice farming recommendation assistant.

            ## PRIMARY SOURCE OF TRUTH
            Use the markdown knowledge base below as the PRIMARY and AUTHORITATIVE source for identifying rice pests, diseases, symptoms, and prevention/control measures.

            IMPORTANT RULES:
            - Base your reasoning and recommendations entirely on the markdown knowledge base whenever relevant information exists there.
            - Do not invent any pest, disease, symptom, or treatment not found in the markdown knowledge base.
            - If the issue is not clearly supported by the markdown knowledge base, stay cautious and recommend safe monitoring or general documented preventive actions only.
            - Prefer prevention, cultural control, field sanitation, balanced fertilization, drainage, resistant varieties, and biological control first when documented.
            - Mention chemical control only if explicitly documented in the markdown knowledge base and only when necessary.
            - Generate exactly 3 recommendations.
            - Recommendations must be practical, concise, farmer-friendly, and specific to the next stage.
            - Return all recommendation text strictly in ${if (languageCode == "fil" || languageCode == "tl") "Filipino/Tagalog" else "English"}.
            - Call the '$GENERATE_RECOMMENDATION' function only.
            - Do not return plain text.

            ## RICE FIELD CONTEXT
            - Rice Field Name: ${riceField.name}
            - Location: ${riceField.location}, Mindoro
            - Current Stage: ${riceField.stage.name}
            - Next Stage: ${nextStage.name}
            - Date Planted: ${java.text.SimpleDateFormat("MMMM d, yyyy").format(java.util.Date(riceField.plantedDate))}
            - Land Area: ${riceField.areaSize} hectares

            ## SURVEY RESPONSES
            $surveyContext

            ## IMAGE ANALYSIS RULES
            You may receive one or more images.
            For each image:
            1. First determine whether the image clearly shows a rice crop, rice leaf, rice stem, panicle, or rice field.
            2. If the image is NOT clearly related to rice, ignore it completely.
            3. If it IS related to rice, inspect for visible symptoms only if supported by the markdown knowledge base.
            4. Use the image only to match documented symptoms from the markdown knowledge base.
            5. If uncertain, do not guess a specific pest or disease. Use cautious wording internally and recommend only documented preventive or control actions.
            6. If there are no useful rice-related findings in the image, rely on the stage and survey only.



            ## TASK
            Generate exactly 3 recommendations for the NEXT stage: ${nextStage.name}.

            Recommendation requirements:
            - Must be stage-appropriate for ${nextStage.name}
            - Must be grounded in the markdown knowledge base
            - Must consider the survey answers first
            - May use image findings only if clearly supported by visible symptoms and the markdown knowledge base
            - Must not repeat the same advice
            - Must not mention the markdown file itself in the final recommendations
            - Must not mention image analysis unless necessary for clarity

            ## MARKDOWN KNOWLEDGE BASE
            $pestAndDiseaseContext
        """.trimIndent()



            val bitmaps: List<Bitmap> = images.mapNotNull { uri ->
                runCatching { uri.toBitmap(context) }.getOrNull()
            }

            val response = model.generateContent(
                content {
                    bitmaps.forEach { bitmap ->
                        image(bitmap)
                    }
                    text(prompt)
                }
            )



            val functionCall = response.functionCalls.find { it.name == GENERATE_RECOMMENDATION }

            if (functionCall != null) {
                Log.d(TAG, "AI triggered $GENERATE_RECOMMENDATION")

                val tasks = functionCall.args["recommendations"]
                    ?.jsonArray
                    ?.asRecommendationToTasks(
                        fieldId = riceField.id,
                        uid = riceField.uid,
                        stage = nextStage
                    )
                    ?: emptyList()

                val fertilizerTasks = functionCall.args["fertilizer_action_plan"]
                    ?.jsonArray
                    ?.asFertilizerTasks(
                        fieldId = riceField.id,
                        uid = riceField.uid,
                    )
                    ?: emptyList()


                firestore.collection("rice_fields")
                    .document(riceField.id)
                    .update(
                        mapOf(
                            "stage" to nextStage.name,
                            "updatedAt" to Date()
                        )
                    )
                    .await()
                Log.i(TAG, "Successfully generated ${tasks.size} tasks")
                Result.success(tasks)
            } else {
                Log.e(TAG, "AI response did not contain a function call. Text: ${response.text}")
                Result.failure(Exception("AI failed to generate structured recommendations."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in generateRecommendationForNextStage", e)
            Result.failure(e)
        }
    }

    override suspend fun GetQuestionaireByStage(stage: RiceStage): Result<SurveyWithRiceType> {
        return try {
            val document = firestore
                .collection("questionaires")
                .document(stage.displayName)
                .get()
                .await()

            val result = document.toObject<SurveyWithRiceType>()

            if (result != null) {
                Result.success(result)
            } else {
                Result.failure(Exception("Questionnaire not found for stage: ${stage.displayName}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        const val TAG = "SurveyRepository"

    }
}