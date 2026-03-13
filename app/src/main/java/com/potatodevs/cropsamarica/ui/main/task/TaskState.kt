package com.potatodevs.cropsamarica.ui.main.task

import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.tasks.Task

data class TaskState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val riceFields : List<RiceFieldWithRiceType> = emptyList(),
    val error: String? = null,
    val selectedIndex : Int = 0,
    val selectedTask : Task? = null,
    val isCreatingTask : Boolean = false
)