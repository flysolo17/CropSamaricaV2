package com.potatodevs.cropsamarica.repositories.pests

import com.potatodevs.cropsamarica.models.pests.PestAndDisease


interface PestRepository {

    suspend fun getAllPests() : Result<List<PestAndDisease>>

    suspend fun getById(id : String) : Result<PestAndDisease?>
}