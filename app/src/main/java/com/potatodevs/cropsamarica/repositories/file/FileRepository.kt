package com.potatodevs.cropsamarica.repositories.file

interface FileRepository {

    suspend fun extractMD(fileName: String): Result<String>

    suspend fun extractMDByStage(
        fileName: String,
        stage: String
    ): Result<String>
}