package com.potatodevs.cropsamarica.repositories.survey

import android.net.Uri
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.rice.RiceStage

import com.potatodevs.cropsamarica.models.survey.Survey
import com.potatodevs.cropsamarica.models.survey.SurveyWithRiceType
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.ui.main.home.subscreens.survey.QuestionWithAnswers


interface SurveyRepository {
    suspend fun generateSurvey(
        id : String,
    ) : Result<Survey>

    suspend fun generateRecommendationForNextStage(
         survey : List<QuestionWithAnswers>,
         crop : RiceFieldWithRiceType,
         images : List<Uri> = emptyList()
    ) : Result<List<Task>>


    suspend fun GetQuestionaireByStage(stage : RiceStage) : Result<SurveyWithRiceType>
}