package com.potatodevs.cropsamarica.ui.main.home.subscreens.survey

import android.net.Uri
import com.potatodevs.cropsamarica.models.survey.Question
import com.potatodevs.cropsamarica.models.survey.Survey
import com.potatodevs.cropsamarica.models.tasks.Task

data class QuestionWithAnswers(
    val question: Question,
    val answer : String ? = null
)

data class QuestionState(
    val isLoading: Boolean = false,
    val survey: Survey? = null,
    val error: String? = null
)
data class SurveyState(
    val isLoading: Boolean = false,
    val recommendations : List<Task> = emptyList(),
    val survey: QuestionState? = null,
    val questionsWithAnswers : List<QuestionWithAnswers> = emptyList(),
    val selectedImage : List<Uri> = emptyList(),
)