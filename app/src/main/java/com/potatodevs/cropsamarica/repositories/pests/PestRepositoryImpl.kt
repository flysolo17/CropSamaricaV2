package com.potatodevs.cropsamarica.repositories.pests

import com.google.firebase.firestore.FirebaseFirestore
import com.potatodevs.cropsamarica.models.pests.PestAndDisease
import com.potatodevs.cropsamarica.ui.utils.PESTS_COLLECTION
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class PestRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PestRepository {

    private val pestsCollection = firestore.collection(PESTS_COLLECTION)

    override suspend fun getAllPests(): Result<List<PestAndDisease>> {
        return try {
            val snapshot = pestsCollection.get().await()
            val pests = snapshot.documents.mapNotNull { doc ->
                doc.toObject(PestAndDisease::class.java)
            }
            Result.success(pests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getById(id: String): Result<PestAndDisease?> {
        return try {
            val doc = pestsCollection.document(id).get().await()
            val pest = doc.toObject(PestAndDisease::class.java)?.copy(id = doc.id)
            Result.success(pest)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
