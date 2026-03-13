package com.potatodevs.cropsamarica.models.survey

import kotlinx.serialization.Serializable


@Serializable
data class Question(

    val text: String ? = null,
    val type: QuestionType? = null,
    val options: List<String>? = null
)
@Serializable
enum class QuestionType {
    MULTIPLE_CHOICE,
    SINGLE_CHOICE,
    SHORT_ANSWER,
    LONG_ANSWER,
    TRUE_FALSE
}
