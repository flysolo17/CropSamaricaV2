package com.potatodevs.cropsamarica.repositories.auth

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.repositories.user.UserRepository
import com.potatodevs.cropsamarica.ui.utils.USERS_COLLECTION
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    val auth : FirebaseAuth,
    val firestore : FirebaseFirestore
): AuthRepository {
    private val userCollection  = firestore.collection(USERS_COLLECTION)
    // In repository
    override suspend fun sendOtp(
        type: AuthEventType,
        activity: Activity,
        phoneNumber: String,
        resendingToken: PhoneAuthProvider.ForceResendingToken ?,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val phone = if (phoneNumber.startsWith("+")) phoneNumber else "+63$phoneNumber"

        if (type == AuthEventType.LOGIN) {
            val snapshot = userCollection
                .whereEqualTo("phone", phoneNumber)
                .limit(1)
                .get()
                .await()

            if (snapshot.isEmpty) {
                throw IllegalStateException("No user is using this phone number")
            }
        }
        if (type == AuthEventType.REGISTER) {
            val snapshot = userCollection
                .whereEqualTo("phone", phoneNumber)
                .limit(1)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                throw IllegalStateException("Another user is already using this phone number")
            }
        }


        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
        if (resendingToken != null) {
            options.setForceResendingToken(resendingToken)
        }
        PhoneAuthProvider.verifyPhoneNumber(options.build())
    }

    override suspend fun verifyOtp(
        verificationId: String,
        otp: String,
        phone: String
    ): Result<AuthResult> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            val result = auth.signInWithCredential(credential).await()
            Result.success(result)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // Wrong OTP entered
            Result.failure(Exception("Invalid OTP. Please try again."))
        } catch (e: FirebaseAuthInvalidUserException) {
            // User account not found or disabled
            Result.failure(Exception("This phone number is not registered."))
        } catch (e: FirebaseTooManyRequestsException) {
            // Quota exceeded
            Result.failure(Exception("Too many attempts. Please wait before retrying."))
        } catch (e: Exception) {
            // Generic fallback
            Result.failure(Exception("Verification failed: ${e.localizedMessage}"))
        }
    }

    override suspend fun register(
        verificationId: String,
        otp: String,
        user: User
    ): Result<String> {
        return try {
            val verifyResult = verifyOtp(verificationId, otp, user.phone)

            verifyResult.fold(
                onSuccess = { authResult ->
                    val firebaseUser = authResult.user
                        ?: return Result.failure(Exception("Verification succeeded but user is null."))

                    val newUser = user.copy(
                        id = firebaseUser.uid,
                        phone = firebaseUser.phoneNumber ?: user.phone
                    )
                    userCollection.document(newUser.id).set(newUser).await()
                    Result.success(newUser.id)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: FirebaseFirestoreException) {
            Result.failure(Exception("Failed to save user: ${e.localizedMessage}"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Invalid OTP. Please try again."))
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("This phone number is not registered or has been disabled."))
        } catch (e: FirebaseTooManyRequestsException) {
            Result.failure(Exception("Too many attempts. Please wait before retrying."))
        } catch (e: Exception) {
            Result.failure(Exception("Registration failed: ${e.localizedMessage}"))
        }
    }
    override suspend fun login(
        verificationId: String,
        otp: String
    ): Result<String> {
        return Result.success("")
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun logout(): Result<String> {
        return try {
            auth.signOut()
            delay(1000)
            Result.success("Logged out successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override  fun listenToUser(): Flow<User?> {
        if (auth.currentUser == null) {
            return callbackFlow {
                trySend(null)
                awaitClose {  }
            }
        }
        return callbackFlow {
            val listener = userCollection.document(auth.currentUser!!.uid)
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
}