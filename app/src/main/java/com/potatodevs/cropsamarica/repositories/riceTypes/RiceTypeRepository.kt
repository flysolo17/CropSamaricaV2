package com.potatodevs.cropsamarica.repositories.riceTypes

import com.potatodevs.cropsamarica.models.rice.RiceType

interface RiceTypeRepository {

    suspend fun getRiceTypes() : List<RiceType>

    suspend fun addYield()
}