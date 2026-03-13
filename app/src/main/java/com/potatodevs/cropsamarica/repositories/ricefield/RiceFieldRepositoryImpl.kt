package com.potatodevs.cropsamarica.repositories.ricefield

import android.R.attr.content
import android.net.Uri
import android.system.Os.close
import android.util.Log
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.potatodevs.cropsamarica.ai.ANOUNCEMENT
import com.potatodevs.cropsamarica.ai.CREATE_ANNOUNCEMENT
import com.potatodevs.cropsamarica.ai.GENERATE_RECOMMENDATION
import com.potatodevs.cropsamarica.ai.converters.asAnnouncement
import com.potatodevs.cropsamarica.ai.converters.asFertilizerTasks
import com.potatodevs.cropsamarica.ai.converters.asRecommendationToTasks
import com.potatodevs.cropsamarica.datastore.LanguageDataStore
import com.potatodevs.cropsamarica.models.announcement.Announcement
import com.potatodevs.cropsamarica.models.fertilizer.fertilizerList
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.rice.RiceType
import com.potatodevs.cropsamarica.models.rice.getYield
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.weather.DailyForecast


import com.potatodevs.cropsamarica.repositories.auth.AuthRepository
import com.potatodevs.cropsamarica.utils.toDateOnly
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose

import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.util.Date
import kotlin.collections.first


class RiceFieldRepositoryImpl @Inject constructor(
    private val firestore : FirebaseFirestore,
    private val authRepository: AuthRepository,
    private val model : GenerativeModel,
    private val storage : FirebaseStorage,
    private val languageDataStore: LanguageDataStore

): RiceFieldRepository {
    override fun getAllByUid(uid: String): Flow<List<RiceFieldWithRiceType>> = callbackFlow {
        val listener = firestore.collection("rice_fields")
            .whereEqualTo("uid", uid)
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val fields = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(RiceField::class.java)
                    }

                    val varietyIds = fields.map { it.variety }.distinct()

                    launch {
                        try {
                            var riceTypes = emptyList<RiceType>()
                            if (varietyIds.isNotEmpty()) {
                                riceTypes = firestore
                                    .collection("varieties")
                                    .whereIn("id", varietyIds)
                                    .get()
                                    .await()
                                    .toObjects<RiceType>()
                            }

                            val results = fields.map {
                                RiceFieldWithRiceType(
                                    riceField = it,
                                    riceType = riceTypes.find { type -> type.id == it.variety } ?: RiceType()
                                )
                            }

                            trySend(results)
                        } catch (e: Exception) {
                            Log.e("enrichedFields", "Failed to fetch varieties", e)
                        }
                    }
                }
            }

        awaitClose { listener.remove() }
    }

    override fun getRiceFieldWithId(riceFieldId: String): Flow<RiceField> {
        return callbackFlow{
            val listener = firestore.collection("rice_fields")
                .document(riceFieldId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }
                    val field = snapshot?.toObject(RiceField::class.java)
                    if (field != null) {
                        trySend(field)
                    }
                }
            awaitClose { listener.remove() }
        }

    }

    override suspend fun deleteCropField(id: String): Result<String> {
        return try {
            val batch = firestore.batch()

            // Delete tasks
            val tasks = firestore.collection("tasks")
                .whereEqualTo("fieldId", id)
                .get()
                .await()
            tasks.documents.forEach { doc ->
                batch.delete(doc.reference)
            }

            // Delete announcements
            val announcements = firestore.collection("announcements")
                .whereEqualTo("fieldId", id)
                .get()
                .await()
            announcements.documents.forEach { doc ->
                batch.delete(doc.reference)
            }

            // Delete reminders (note: uses "riceFieldId")
            val reminders = firestore.collection("reminders")
                .whereEqualTo("riceFieldId", id)
                .get()
                .await()
            reminders.documents.forEach { doc ->
                batch.delete(doc.reference)
            }


            val fieldRef = firestore.collection("rice_fields").document(id)
            batch.delete(fieldRef)


            batch.commit().await()

            Result.success("Crop field deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun create(
        riceField: RiceField,
        selectedRiceType : RiceType,
        image: Uri?,
        dailyHighLow: List<String>,
    ): Result<List<Task>> {
        return try {
            val  batch = firestore.batch()
            val uid = authRepository.getCurrentUser()?.uid
                ?: return Result.failure(Exception("User not authenticated"))

            val docRef = firestore.collection("rice_fields").document()
            riceField.uid = uid
            riceField.id = docRef.id
            val languageCode = languageDataStore.languageFlow.first().lowercase()
            val fertilizerContext = fertilizerList.joinToString("\n") {
                "- Stage: ${it.stage}, Action: ${it.amount} of ${it.type}, Timing: ${it.timing}, Purpose: ${it.purpose}"
            }
            val yield = riceField.getYield(selectedRiceType)
            val text =                     """
                                ### INPUT DATA
                                - Rice Field: ${riceField.name}
                                - Location: ${riceField.location}, Mindoro
                                - Growth Stage: ${riceField.stage.displayName}
                                - Date Planted: ${Date(riceField.plantedDate)}
                                - Land Area: ${riceField.areaSize}
                                - Projected Yield: $yield tons
                                - Language: $languageCode
                                
                                ### WEATHER DATA (Next 3 Days)
                                ${dailyHighLow.joinToString("\n")}
                                
                                ### FERTILIZER SCHEDULE (PhilRice Standard)
                                $fertilizerContext
                                
                                ### TASK
                                Generate 3 rice farming recommendations based on the input data and weather.
                                Generate a fertilizer application plan for the remaining stages based on the area size (${riceField.areaSize}).
                                
                                CRITICAL INSTRUCTIONS:
                                1. Provide the response strictly in the language: ${if(languageCode == "fil") "Filipino/Tagalog" else "English"}.
                                2. Use the '${GENERATE_RECOMMENDATION}' function.
                                3. Each recommendation 'details' must be exactly 1-2 sentences.
                                4. Calculate the total fertilizer needed for the remaining stages based on the area size provided.
                                5. For the 'fertilizer_action_plan' date, calculate the specific date based on the 'Date Planted' and the 'timing' indicated in the schedule.
                        """.trimIndent()
            val prompt = content {
                text(text)
            }
            Log.d(
                TAG,
                text
            )
            var recommendations = emptyList<Task>()
            val response = model.generateContent(prompt)
            response.functionCalls
                .find { it.name == GENERATE_RECOMMENDATION }
                ?.let {
                    val filtelizerTasks = it.args["fertilizer_action_plan"]?.jsonArray?.asFertilizerTasks(
                        fieldId = docRef.id,
                        uid = uid
                    ) ?: emptyList()
                     recommendations = it.args["recommendations"]?.jsonArray?.asRecommendationToTasks(
                        fieldId = docRef.id,
                        uid = uid,
                        stage = riceField.stage
                    ) ?: emptyList()
                    filtelizerTasks.forEach {

                        val taskRef = firestore.collection("tasks").document()
                        it.id = taskRef.id
                        batch.set(taskRef, it)
                    }
                } ?: return Result.failure(Exception("Error cannot generate recommendations"))
            batch.set(docRef, riceField)
            if (image != null) {
                val imageRef = storage.reference
                    .child("rice_fields")
                    .child(docRef.id)

                val uploadTask = imageRef.putFile(image).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await()
                batch.update(docRef, "image", downloadUrl.toString())
            }
            batch.commit().await()
            Result.success(recommendations)
        } catch (e: Exception) {
            Log.e(TAG, "create: $e")
            Result.failure(e)
        }
    }

    override suspend fun getById(id: String): Result<RiceFieldWithRiceType> {
        return try {
            val field = firestore.collection("rice_fields")
                .document(id)
                .get()
                .await()
                .toObject(RiceField::class.java)
            val variety = firestore.collection("varieties")
                .document(field?.variety.orEmpty())
                .get()
                .await()
                .toObject(RiceType::class.java)
            if (field != null && variety != null) {
                Result.success(RiceFieldWithRiceType(field, variety))
            } else {
                Result.failure(Exception("Field or variety not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateAnnouncement(
        riceField: RiceFieldWithRiceType,
        forecast: DailyForecast
    ): Result<Announcement> = runCatching {
        val announcementRef = firestore.collection("announcements")
        val today = Date().toDateOnly()

        // 1. Check for existing announcement for today
        val existing = announcementRef
            .whereEqualTo("fieldId", riceField.riceField?.id)
            .whereEqualTo("date", today)
            .limit(1)
            .get()
            .await()

        if (!existing.isEmpty) {
            return@runCatching existing.documents.first().toObject(Announcement::class.java)
                ?: throw Exception("Failed to deserialize existing announcement")
        }

        // 2. Build the AI Prompt (Tasks removed)
        val prompt = content {
            text("""
            Generate exactly one clear, actionable announcement for a farmer.
            Context:
            - Rice Variety: ${riceField.riceType?.name}
            - Growth Stage: ${riceField.riceField?.stage}
            - Weather: ${forecast.condition}, High of ${forecast.highLow}°C
            
            Provide the response in English (en) and Tagalog (tl).
            Format as JSON:
            {
              "announcement": {
                "en": { "title": "...", "message": "...", "urgency": "LOW|MEDIUM|HIGH" },
                "tl": { "title": "...", "message": "...", "urgency": "LOW|MEDIUM|HIGH" }
              }
            }
        """.trimIndent())
        }

        // 3. Generate and Parse
        val response = model.generateContent(prompt)

        // Safety check for Function Calling or Text response
        val functionCall = response.functionCalls.find { it.name == ANOUNCEMENT }
        val announcementJson = functionCall?.args?.get("announcement")?.jsonObject
            ?: throw Exception("AI failed to provide a valid announcement object")

        // 4. Map to Model and Save
        val newAnnouncement = announcementJson.asAnnouncement(
            fieldId = riceField.riceField?.id!!,
        ).copy(
            id = announcementRef.document().id,
            date = today
        )

        announcementRef.document(newAnnouncement.id)
            .set(newAnnouncement)
            .await()

        newAnnouncement
    }

    companion object {
        const val TAG = "RiceFieldRepository"
    }
}
