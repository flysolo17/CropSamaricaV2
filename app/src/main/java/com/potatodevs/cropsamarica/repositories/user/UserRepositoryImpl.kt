package com.potatodevs.cropsamarica.repositories.user

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.ui.utils.UIState
import com.potatodevs.cropsamarica.ui.utils.USERS_COLLECTION
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl @Inject constructor(
    private val firestore : FirebaseFirestore,
    private val auth : FirebaseAuth,
    private val storage : FirebaseStorage
) : UserRepository {
    private val _storageRef = storage.reference
    private val userCollection  = firestore.collection(USERS_COLLECTION)
    override suspend fun save(user: User, image: Uri?): Result<String> {
        return try {
            if (image != null) {
                val storageRef = _storageRef.child("profiles/${user.id}")
                val uploadTask = storageRef.putFile(image).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await()
                user.profile  = downloadUrl.toString()
            }

            userCollection.document(user.id).set(user).await()
            Result.success(user.id)
        } catch (e: Exception) {
            // Wrap the exception in a failure Result
            Result.failure(e)
        }
    }

    override suspend fun getUser(id: String): Flow<User?> {
        return callbackFlow {
            val listener = userCollection.document(id)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {

                        close(error)
                        return@addSnapshotListener
                    }
                    trySend(snapshot?.toObject(User::class.java))
                }
            awaitClose { listener.remove() }
        }

    }

    override suspend fun changeName(
        uid: String,
        name: String,
        result: (UIState<String>) -> Unit
    ) {
        result.invoke(UIState.Loading)
        delay(1000)
        userCollection.document(uid).update("name", name).addOnCompleteListener {
            if (it.isSuccessful) {
                result.invoke(UIState.Success("Name changed successfully"))
            } else {
                result.invoke(UIState.Error(it.exception?.message ?: "Unknown error"))
            }
        }.addOnFailureListener {
            result.invoke(UIState.Error(it.message ?: "Unknown error"))
        }
    }

    override suspend fun uploadProfile(image: Uri): Result<String> {
        return try {
            val user =
                auth.currentUser ?: return Result.failure(Exception("No authenticated user found."))
            val storageRef = _storageRef.child("profiles/${user.uid}")
            val uploadTask = storageRef.putFile(image).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            userCollection.document(user.uid).update("profile", downloadUrl.toString()).await()
            delay(1000)
            Result.success("Profile image uploaded successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFarmer(): Result<User?> {
        if (auth.currentUser == null) {
            return Result.failure(Exception("No authenticated user found."))
        }
        return try {
            val user = userCollection.document(auth.currentUser!!.uid).get().await()
                .toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}