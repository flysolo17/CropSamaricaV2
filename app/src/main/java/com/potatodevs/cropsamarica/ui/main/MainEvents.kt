package com.potatodevs.cropsamarica.ui.main

import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType

sealed interface MainEvents {

    data class SelectRiceField(val riceField: RiceFieldWithRiceType) : MainEvents

    data class GetRiceFields(
        val uid : String
    ) : MainEvents


}