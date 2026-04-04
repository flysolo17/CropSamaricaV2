package com.potatodevs.cropsamarica.models.tasks

import com.potatodevs.cropsamarica.models.rice.RiceStage

import java.util.Date



data class Task(
    var id: String = "",
    var uid : String = "",
    var fieldId: String = "",
    val fertilizer : Boolean ? = null,
    val type : String ? = null,
    val amount : String ? = null,
    val title: String = "",
    val description: String = "",
    val stage : RiceStage = RiceStage.SEEDLING,
    val status: TaskStatus = TaskStatus.PENDING,
    val startDate : Date ? = null,
    val dueDate : Date ?= null,
    val createdAt : Date = Date(),
    val updatedAt : Date = Date(),
)

