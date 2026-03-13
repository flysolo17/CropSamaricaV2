package com.potatodevs.cropsamarica.repositories.survey

import android.util.Log
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import com.google.firebase.firestore.FirebaseFirestore
import com.potatodevs.cropsamarica.ai.GENERATE_RECOMMENDATION
import com.potatodevs.cropsamarica.ai.SURVEY_GENERATION
import com.potatodevs.cropsamarica.ai.converters.asRecommendationToTasks
import com.potatodevs.cropsamarica.ai.converters.asSurvey
import com.potatodevs.cropsamarica.datastore.LanguageDataStore
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.survey.Question
import com.potatodevs.cropsamarica.models.survey.Survey
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import com.potatodevs.cropsamarica.repositories.weather.WeatherRepository
import com.potatodevs.cropsamarica.ui.main.home.subscreens.survey.QuestionWithAnswers
import com.potatodevs.cropsamarica.ui.utils.getRiceStage
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.Date

class SurveyRepositoryImpl
    @Inject constructor(
        private val firestore: FirebaseFirestore,
        private val riceFieldRepository: RiceFieldRepository,
        private val model : GenerativeModel,
        private val weatherRepository: WeatherRepository,
        private val languageDataStore: LanguageDataStore
    )
    : SurveyRepository {
    override suspend fun generateSurvey(id: String): Result<Survey> {
        Log.d(TAG, "Starting survey generation for Field ID: $id")

        return try {
            val field = riceFieldRepository.getById(id).getOrThrow()

            val text = """
            The farmer is currently growing rice in the '${field.riceField?.stage}' stage. 
            The variety is ${field.riceType?.name}. 
            Based on this context, generate a targeted diagnostic survey to determine 
            if they are ready for the next crop stage or if intervention is needed.
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

    override suspend fun generateRecommendationForNextStage(
        survey: List<QuestionWithAnswers>,
        crop: RiceFieldWithRiceType
    ): Result<List<Task>> {
        Log.d(TAG, "Generating recommendations for: ${crop.riceField?.name}")

        return try {
            val nextStage = Date(crop.riceField?.plantedDate!!).getRiceStage()
            val languageCode = languageDataStore.languageFlow.first().lowercase()
            val riceField = crop.riceField
            firestore.collection("rice_fields")
                .document(
                    crop.riceField.id
                ).update(
                    "stage", nextStage.name,
                    "updatedAt", Date()
                ).await()
            val surveyContext = survey.joinToString("\n") { "Q: ${it.question.text} | A: ${it.answer}" }

            val text = """
            ### INPUT DATA
            - Rice Field: ${riceField.name}
            - Location: ${riceField.location}, Mindoro
            - Growth Stage: ${riceField.stage.name}
            - Date Planted: ${java.text.SimpleDateFormat("MMMM d, yyyy").format(java.util.Date(riceField.plantedDate))}
            - Land Area: ${riceField.areaSize} hectares
            - Language: $languageCode
            
            ### FARMER SURVEY RESPONSES
            $surveyContext

            ### TASK
            Based on the current stage (${riceField.stage.name}) and the survey answers, generate 3 specific recommendations.
            
            CRITICAL INSTRUCTIONS:
            1. Provide the response strictly in: ${if(languageCode == "fil") "Filipino/Tagalog" else "English"}.
            2. Call the '${GENERATE_RECOMMENDATION}' function.
            3. Do not return plain text.
        """.trimIndent()

            val response = model.generateContent(content { text(text) })


            val functionCall = response.functionCalls.find { it.name == GENERATE_RECOMMENDATION }

            if (functionCall != null) {
                Log.d(TAG, "AI triggered $GENERATE_RECOMMENDATION")

                // Map the 'recommendations' array from AI args to your Task list
                val tasks = functionCall.args["recommendations"]?.jsonArray?.asRecommendationToTasks(
                    fieldId = riceField.id,
                    uid = riceField.uid,
                    stage = nextStage
                ) ?: emptyList()

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

    companion object {
        const val TAG = "SurveyRepository"

    }
}