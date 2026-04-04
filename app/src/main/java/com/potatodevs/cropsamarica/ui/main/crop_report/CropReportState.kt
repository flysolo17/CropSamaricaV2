package com.potatodevs.cropsamarica.ui.main.crop_report

import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.tasks.Task


data class CropReportState(
    val isLoading : Boolean = false,
    val crop : RiceFieldWithRiceType? = null,
    val tasks : FertilizerState = FertilizerState(),
    val farmer : FarmerState = FarmerState()
)

data class FarmerState(
    val isLoading : Boolean = false,
    val user : User? = null
)

data class FertilizerState(
    val isLoading : Boolean = false,
    val tasks : List<Task> = emptyList()

)