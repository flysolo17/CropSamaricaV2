package com.potatodevs.cropsamarica.models.reminder

import com.potatodevs.cropsamarica.models.rice.RiceStage
import java.util.Date

data class Reminder(
    var id : String = "",
    var uid : String = "",
    val riceFieldId : String = "",
    val message : String = "",
    val bestApplicationTime :List<BestApplicationTime> = emptyList(),
    val reminderDate : Date = Date(),
    val stage : RiceStage = RiceStage.SEEDLING,
    val createdAt : Date = Date(),
)