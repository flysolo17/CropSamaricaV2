package com.potatodevs.cropsamarica.models.tasks

import com.potatodevs.cropsamarica.models.rice.RiceStage


data class RiceFieldTask(
    val name : String = "",
    val stage : RiceStage = RiceStage.SEEDLING,
    val tasks : List<Task> = emptyList()
)