package com.potatodevs.cropsamarica.repositories.auth

import android.app.Activity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import com.potatodevs.cropsamarica.models.User
import kotlinx.coroutines.flow.Flow

enum class AuthEventType {
    LOGIN,
    REGISTER

}
interface AuthRepository {
    suspend fun sendOtp(
        type: AuthEventType = AuthEventType.REGISTER,
        activity: Activity,
        phoneNumber: String,
        resendingToken: PhoneAuthProvider.ForceResendingToken? = null,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    )

    suspend fun verifyOtp(
        verificationId: String,
        otp: String,
        phone : String,

    ) : Result<AuthResult>

    suspend fun register(
        verificationId: String,
        otp: String,
        user: User
    )   : Result<String>

    suspend fun login(
        verificationId: String,
        otp : String
    )  : Result<String>

    suspend fun getCurrentUser() : FirebaseUser ?



    suspend fun logout(): Result<String>

     fun listenToUser() : Flow<User?>
}