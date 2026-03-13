package com.potatodevs.cropsamarica.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Condition(
    val text: String = "",
    val icon: String = ""
)
