package com.potatodevs.cropsamarica.ai.converters


import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.tasks.TaskStatus
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun JsonArray.asFertilizerTasks(
    fieldId: String,
    uid: String
): List<Task> {
    val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.US)

    return this.map { element ->
        val obj = element.jsonObject

        // Extract values using kotlinx.serialization
        val title = obj["title"]?.jsonPrimitive?.content ?: "Application Task"
        val purpose = obj["purpose"]?.jsonPrimitive?.content ?: ""
        val type = obj["type"]?.jsonPrimitive?.content ?: ""
        val amount = obj["amount"]?.jsonPrimitive?.content ?: ""
        val rawDate = obj["date"]?.jsonPrimitive?.content ?: ""
        val stageString = obj["stage"]?.jsonPrimitive?.content ?: "SEEDLING"

        val taskStage = runCatching { RiceStage.valueOf(stageString) }
            .getOrDefault(RiceStage.TILLERING)
        val parsedDate = runCatching { dateFormat.parse(rawDate) }.getOrDefault(Date())

        Task(
            fieldId = fieldId,
            uid = uid,
            fertilizer = true,
            title = title,
            type = type,
            amount = amount,
            description = purpose,
            stage = taskStage,
            status = TaskStatus.PENDING,
            startDate = parsedDate
        )
    }
}
