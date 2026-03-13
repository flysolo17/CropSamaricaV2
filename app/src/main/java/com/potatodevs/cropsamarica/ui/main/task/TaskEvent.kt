package com.potatodevs.cropsamarica.ui.main.task

import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.tasks.Task

import com.potatodevs.cropsamarica.ui.utils.UIState

sealed interface TaskEvent {
    data object LoadTask: TaskEvent

    data class OnSelected(
        val index : Int
    ) : TaskEvent

    data class OnCreateTask(
        val task : Task,
        val result : (UIState<String>) -> Unit
    ) : TaskEvent

    data class OnDeleteTask(
        val taskId : String,
    ) : TaskEvent
    data class OnUpdateTask(
        val task : Task,
    ) : TaskEvent
    data class OnTaskSelected(
        val task : Task?
    ) : TaskEvent
}