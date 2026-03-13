package com.potatodevs.cropsamarica.models.rice

data class RiceType(
    val id : String = "",
    val name : String = "",
    val description : String = "",
    val maturity : Int = 100,
    val image : String = "",
    val yieldPerHectare : Double = 0.0,
)