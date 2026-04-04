package com.potatodevs.cropsamarica.ai.converters


import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.tasks.TaskStatus
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun JsonArray.asRecommendationToTasks(
    fieldId: String,
    uid: String,
    stage: RiceStage
): List<Task> {
    return this.map { element ->
        val obj = element.jsonObject

        val title = obj["title"]?.jsonPrimitive?.content ?: "Farm Advisory"
        val details = obj["details"]?.jsonPrimitive?.content ?: ""
        val amount = obj["amount"]?.jsonPrimitive?.content
        Task(
            fieldId = fieldId,
            amount = amount,
            uid = uid,
            fertilizer = false,
            title = title,
            description = details,
            stage = stage,
            status = TaskStatus.PENDING,
        )
    }
}