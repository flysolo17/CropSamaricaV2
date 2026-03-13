package com.potatodevs.cropsamarica.ui.developer

import androidx.annotation.DrawableRes
import com.potatodevs.cropsamarica.R


data class Developer(
    val name : String,
    val email : String,
    @DrawableRes val profile : Int,
    val role : String
)

val DEVELOPERS = listOf(
    Developer(
        name = "Mary Joy A. Saulong",
        email = "maryjoysaulong.bsit@gmail.com",
        profile = R.drawable.mary,
        role = "Programmer"
    ),
    Developer(
        name = "Roshiene T. Orongan",
        email = "roshieneorongan.bsit@gmail.com",
        profile = R.drawable.rosh,
        role = "UI/UX Designer"
    ),
    Developer(
        name = "Marvie Angela F. Cainglet",
        email = "marvieangelacainglet.bsit@gmail.com",
        profile = R.drawable.marvie,
        role = "Researcher"
    ),
    Developer(
        name = "Noli G. De San Jose, Jr.",
        email = "nolidesanjose360@gmail.com",
        profile = R.drawable.noli,
        role = "Documentation Specialist"
    )
)