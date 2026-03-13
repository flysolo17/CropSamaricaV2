package com.potatodevs.cropsamarica.ai.converters

import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.survey.Question
import com.potatodevs.cropsamarica.models.survey.QuestionType
import com.potatodevs.cropsamarica.models.survey.Survey

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
fun Map<String, JsonElement>.asSurvey(crop : RiceFieldWithRiceType): Survey? {
    val purpose = this["survey_goal"]?.jsonPrimitive?.content ?: "General Crop Assessment"
    val questions = this["questions"]?.jsonArray?.mapNotNull { element ->
        val obj = element.jsonObject
        val text = obj["text"]?.jsonPrimitive?.content ?: return@mapNotNull null
        val typeStr = obj["type"]?.jsonPrimitive?.content ?: return@mapNotNull null
        val type = runCatching { QuestionType.valueOf(typeStr.uppercase()) }.getOrNull()
            ?: QuestionType.SHORT_ANSWER

        val options = obj["options"]?.jsonArray?.mapNotNull { it.jsonPrimitive.content }

        Question(text, type, options)
    } ?: emptyList()

    return if (questions.isEmpty()) null else Survey(purpose, crop = crop,questions)
}