package com.potatodevs.cropsamarica.ai.converters

import com.potatodevs.cropsamarica.models.reminder.ApplicationCondition
import com.potatodevs.cropsamarica.models.reminder.BestApplicationTime
import com.potatodevs.cropsamarica.models.reminder.Reminder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.Locale

fun JsonObject.asReminder(): Reminder {
    // Define the date format expected from the AI.
    // Using Locale.US is generally safer for machine-generated strings to avoid locale issues.
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale("en", "PH")
    ) // Changed Locale.CHINA to Locale.US for consistency
    val id = this["id"]?.jsonPrimitive?.contentOrNull
        ?: throw IllegalArgumentException("Reminder 'id' is missing or not a valid string.")
    val riceFieldId = this["riceFieldId"]?.jsonPrimitive?.contentOrNull
        ?: throw IllegalArgumentException("Reminder 'riceFieldId' is missing or not a valid string.")
    val message = this["message"]?.jsonPrimitive?.contentOrNull
        ?: throw IllegalArgumentException("Reminder 'message' is missing or not a valid string.")

    // Process the 'bestApplicationTime' array
    val bestApplicationTime = this["bestApplicationTime"]?.jsonArray?.map { element ->
        // Each element in the array should be a JsonObject
        val timeObj = element.jsonObject
        val time = timeObj["time"]?.jsonPrimitive?.contentOrNull
            ?: throw IllegalArgumentException("BestApplicationTime 'time' is missing or not a valid string.")
        val conditionString = timeObj["condition"]?.jsonPrimitive?.contentOrNull
            ?: throw IllegalArgumentException("BestApplicationTime 'condition' is missing or not a valid string.")

        // Convert the string to the ApplicationCondition enum
        val condition = try {
            ApplicationCondition.valueOf(conditionString)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid ApplicationCondition value: '$conditionString'", e)
        }
        BestApplicationTime(time = time, condition = condition)
    } ?: emptyList() // If 'bestApplicationTime' is missing or malformed, default to an empty list

    // Extract and parse the reminderDate
    val reminderDateString = this["reminderDate"]?.jsonPrimitive?.contentOrNull
        ?: throw IllegalArgumentException("Reminder 'reminderDate' is missing or not a valid string.")

    val reminderDate = dateFormat.parse(reminderDateString)

    return Reminder(
        id = id,
        riceFieldId = riceFieldId,
        message = message,
        bestApplicationTime = bestApplicationTime,
        reminderDate = reminderDate
    )
}