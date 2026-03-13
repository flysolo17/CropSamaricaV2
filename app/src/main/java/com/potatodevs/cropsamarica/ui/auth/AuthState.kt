package com.potatodevs.cropsamarica.ui.auth

import android.net.Uri
import com.google.firebase.auth.PhoneAuthProvider
import com.potatodevs.cropsamarica.models.Municipality
import com.potatodevs.cropsamarica.repositories.auth.AuthEventType
import java.io.File

data class LoginState(
    val phone : String = "",

)
data class RegisterState(
    val name : String = "",
    val selectedMunicipality: Municipality ? = null,
    val selectedBarangay : String ? = null,

    val phone : String = "",
    val selectedImage : Uri? = null
)
data class AuthState(
    val phone : String ="",
    val isLoading : Boolean = false,
    val loginState : LoginState = LoginState(),
    val registerState : RegisterState = RegisterState(),
    val storedVerificationId : String? = null,
    val authType : AuthEventType? = null,
    val resendToken : PhoneAuthProvider.ForceResendingToken? = null,
    val otp : String = "",
    val timer : Long = 0L
)