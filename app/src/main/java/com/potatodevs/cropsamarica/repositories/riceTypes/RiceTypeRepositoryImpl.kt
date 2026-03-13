package com.potatodevs.cropsamarica.repositories.riceTypes

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.potatodevs.cropsamarica.models.rice.RiceType
import com.potatodevs.cropsamarica.ui.utils.VARIETIES_COLLECTION
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class RiceTypeRepositoryImpl  @Inject constructor(
    private val firestore : FirebaseFirestore
): RiceTypeRepository {
    private val riceTypeCollection = firestore.collection(VARIETIES_COLLECTION)

    override suspend fun getRiceTypes(): List<RiceType> {
        return try {
            val querySnapshot = riceTypeCollection.get().await()
            querySnapshot.toObjects(
                RiceType::class.java
            )
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addYield() {


    }
}