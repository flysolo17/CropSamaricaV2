package com.potatodevs.cropsamarica.ui.main.home.subscreens.survey

import android.net.Uri
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.tasks.Task


sealed interface SurveyEvents {
    data class OnGenerateSurvey(
        val id: String
    ) : SurveyEvents



    data class OnChangeAnswer(val index: Int, val answer: String) :
        SurveyEvents
    data class OnImageChange(
        val image : Uri
    ) : SurveyEvents

    data object OnSubmit : SurveyEvents

    data class OnSaveTasks(
        val tasks : List<Task>
    ) : SurveyEvents

}
