package com.potatodevs.cropsamarica.ui.main

import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.tasks.Task



data class MainState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val counter : Int = 0,
    val riceFields : List<RiceFieldWithRiceType> = emptyList(),
    val selectedRiceField : RiceFieldWithRiceType? = null,

)
