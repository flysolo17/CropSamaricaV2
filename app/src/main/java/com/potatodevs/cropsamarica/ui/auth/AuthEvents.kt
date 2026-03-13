package com.potatodevs.cropsamarica.ui.auth

import android.app.Activity
import android.net.Uri
import com.google.firebase.auth.PhoneAuthProvider
import com.potatodevs.cropsamarica.models.Municipality
import com.potatodevs.cropsamarica.repositories.auth.AuthEventType
import java.io.File


sealed interface AuthEvents {
    data class OnSendCode(
        val type : AuthEventType,
        val activity: Activity,
        val phone : String,
    ) : AuthEvents

    data class OnCodeSent(
        val storedVerificationId : String ?= null,
        val resendToken : PhoneAuthProvider.ForceResendingToken ? = null
    ) : AuthEvents

    data class OnVerify(
        val type: AuthEventType,
        val  otp : String,
        val resendToken : PhoneAuthProvider.ForceResendingToken ? = null
    ): AuthEvents
    data class OnPhoneChange(
        val phone : String,
        val type : AuthEventType
    ) : AuthEvents

    sealed interface RegisterEvents : AuthEvents {
        data class OnNameChange(val name : String) : RegisterEvents
        data class OnMunicipalityChange(val municipality: Municipality) : RegisterEvents
        data class OnBarangayChange(val barangay : String) : RegisterEvents
        data class OnPhoneChange(val phone : String) : RegisterEvents
        data class OnImageSelected(
            val image : Uri ?
        ) : RegisterEvents
    }
}