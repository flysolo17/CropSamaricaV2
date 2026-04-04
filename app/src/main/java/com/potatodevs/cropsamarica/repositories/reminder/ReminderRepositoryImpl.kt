package com.potatodevs.cropsamarica.repositories.reminder

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.potatodevs.cropsamarica.ai.REMINDER
import com.potatodevs.cropsamarica.ai.converters.asReminder
import com.potatodevs.cropsamarica.models.reminder.Reminder
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.weather.SevenDayWeatherResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.util.Calendar
import javax.inject.Inject
import kotlin.collections.orEmpty

class ReminderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth,
    private val model  : GenerativeModel
): ReminderRepository {
    private val remindersCollection = firestore.collection("reminders")

    override suspend fun generateReminder(
        forecast: SevenDayWeatherResponse,
        riceField: RiceFieldWithRiceType
    ) : Result<List<Reminder>> {
        val prompt = content {
            text(
                """
        Generate reminders for the given rice field using the rice field data and 7-day weather forecast.

        Requirements:
        - Generate at least 2 distinct reminders.
        - Every reminder must be actionable, practical, and useful for the farmer.
        - Do not generate reminders related to heavy rain.
        - Do not generate vague reminders unless they include a concrete action.
        - Use both the rice field data and forecast when deciding each reminder.
        - Tailor each reminder to the rice field stage or condition when available.
        - Avoid duplicate reminders or repeated meanings.

        Field requirements for each reminder:
        - id: generate a unique identifier string for the reminder.
        - riceFieldId: use the rice field ID from the provided rice field data.
        - stage: use the rice field stage from the provided rice field data if available.
        - message: write a short, clear, practical, concise, and farmer-friendly reminder message.
        - reminderDate: must be between today and the next 6 days inclusive, in yyyy-MM-dd format only.
        - bestApplicationTime: must contain exactly 16 entries, one for each hour from 6 AM to 9 PM inclusive.

        bestApplicationTime rules:
        - Include exactly these hours:
          6 AM, 7 AM, 8 AM, 9 AM, 10 AM, 11 AM, 12 PM, 1 PM, 2 PM, 3 PM, 4 PM, 5 PM, 6 PM, 7 PM, 8 PM, 9 PM
        - For each hour, set condition to exactly one of:
          OPTIMAL, MODERATE, UNFAVORABLE
        - Determine suitability using the forecast for the reminderDate.
        - Mark hours as:
          - OPTIMAL when weather is most suitable for doing the action
          - MODERATE when the action may still be done but with some caution
          - UNFAVORABLE when weather conditions make the action unsuitable
        - Ensure all 16 hours are present and ordered chronologically.

        Output rules:
        - Return the result only through the $REMINDER function call.
        - Do not return plain text.
        - Ensure all generated values strictly follow the declared schema.

        Rice Field Data:
        ${riceField}

        Weather Forecast:
        ${forecast}
        """.trimIndent()
            )
        }
        Log.d("AyaRepositoryImpl", "generateReminder: $prompt")
        val response = model.generateContent(prompt)
        val functionCalls = response.functionCalls.find { it.name == REMINDER }
        Log.d("AyaRepositoryImpl", "generateReminder: ${functionCalls?.args}")
        if (functionCalls != null) {
            val recommendations = functionCalls.args["reminders"]
                ?.jsonArray
                ?.mapNotNull { recElement ->
                    recElement.jsonObject.asReminder()
                } ?: emptyList()
            Log.d("AyaRepositoryImpl", "Recommendations: $recommendations")
            return  Result.success(recommendations)
        } else {
           return Result.failure(Exception("No function calls found"))
        }
    }

    override suspend fun createReminders(reminder: Reminder): Result<String> {
        return try {
            val uid = auth.currentUser?.uid ?: throw IllegalStateException("User not found")
            reminder.id = remindersCollection.document().id
            reminder.uid = uid
            remindersCollection.document(reminder.id).set(reminder).await()
            Result.success("Task inserted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override fun getRemindersToday(id: String) = callbackFlow {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = Timestamp(calendar.time)

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        val endOfDay = Timestamp(calendar.time)

        val listener = remindersCollection
            .whereEqualTo("riceFieldId", id)
            .whereGreaterThanOrEqualTo("reminderDate", startOfDay)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("TaskRepositoryImpl", "getRemindersToday error: ", error)
                    trySend(emptyList()).isSuccess
                    return@addSnapshotListener
                }
                val reminders = snapshot?.toObjects(Reminder::class.java).orEmpty()
                trySend(reminders).isSuccess
            }
        awaitClose { listener.remove() }

    }
}