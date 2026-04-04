package com.potatodevs.cropsamarica.repositories.file

import android.content.Context
import android.util.Log
import jakarta.inject.Inject

class FileRepositoryImpl @Inject constructor(
    val context: Context
) : FileRepository {
    override suspend fun extractMD(fileName: String): Result<String> {
        return try {
            val content = context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }
            Result.success(content)
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting MD file", e)
            Result.failure(e)
        }
    }

    override suspend fun extractMDByStage(
        fileName: String,
        stage: String
    ): Result<String> {
        return try {
            val fullText = extractMD(fileName).getOrThrow()
            val stageSection = extractStageSection(fullText, stage)
            Log.d(TAG, "Extracted stage section: $stageSection")

            Result.success(stageSection)

        } catch (e: Exception) {
            Log.e(TAG, "Error extracting stage section", e)
            Result.failure(e)
        }
    }

    private fun extractStageSection(markdown: String, stage: String): String {
        val normalizedStage = stage.uppercase()

        val startIndex = markdown.indexOf("## $normalizedStage")
        if (startIndex == -1) return ""

        val remaining = markdown.substring(startIndex)
        val endIndex = remaining.indexOf("\n## ", startIndex = 1)

        return if (endIndex != -1) {
            remaining.substring(0, endIndex)
        } else {
            remaining
        }
    }
    companion object {
        const val TAG = "FileRepository"

    }
}