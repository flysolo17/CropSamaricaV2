package com.potatodevs.cropsamarica.models.reminder

data class BestApplicationTime(
    val time : String = "",
    val condition : ApplicationCondition = ApplicationCondition.OPTIMAL
)