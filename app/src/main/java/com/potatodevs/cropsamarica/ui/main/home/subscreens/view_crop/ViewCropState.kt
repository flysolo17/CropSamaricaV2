package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop

import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.tasks.Task


data class ViewCropState(
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