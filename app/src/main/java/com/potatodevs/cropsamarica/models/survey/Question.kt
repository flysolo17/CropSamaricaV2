package com.potatodevs.cropsamarica.models.survey

import com.potatodevs.cropsamarica.models.pests.LocalizeText
import com.potatodevs.cropsamarica.models.rice.RiceStage
import kotlinx.serialization.Serializable


data class SurveyWithRiceType(
    var id : String = "",
    val stage : RiceStage = RiceStage.SEEDLING,
    val questions : List<Question> = emptyList()
)

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
