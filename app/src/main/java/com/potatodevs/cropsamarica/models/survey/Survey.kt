package com.potatodevs.cropsamarica.models.survey

import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType


data class Survey(
    val purpose : String,
    val crop : RiceFieldWithRiceType?,
    val questions : List<Question>
)