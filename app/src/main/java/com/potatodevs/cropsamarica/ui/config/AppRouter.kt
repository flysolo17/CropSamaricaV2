package com.potatodevs.cropsamarica.ui.config

import androidx.navigation3.runtime.NavKey
import com.google.firebase.auth.PhoneAuthProvider
import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.repositories.auth.AuthEventType
import kotlinx.serialization.Serializable


sealed interface AppRouter : NavKey {
    data object OnBoarding : AppRouter
    data object LanguageSelection : AppRouter
    data object Auth : AppRouter
    @Serializable
    data object UserGuide : AppRouter

    @Serializable
    data object CreateRiceField : AppRouter

    @Serializable
    data class Verification(
        val phone : String,
        val storedVerificationId : String ?= null,
        val resendToken : String ? = null,
        val type : AuthEventType = AuthEventType.LOGIN
    )

    @Serializable
    data object Settings : AppRouter

    @Serializable
    data object Notification : AppRouter

    @Serializable
    data class ViewNotification(
        val id : String
    ): AppRouter

    sealed interface Main : AppRouter {
        @Serializable
        data object CropReport: Main



        @Serializable
        data class Index(
            val uid : String
        ) : Main

        @Serializable
        data object Dashboard : Main

        @Serializable
        data object PestAndDisease : Main

        @Serializable
        data class PestDetails(
            val id : String
        ) : Main

        @Serializable
        data object Task : Main

        @Serializable
        data object Profile : NavKey

        @Serializable
        data object Developer : NavKey

        @Serializable
        data object UnknownError : Main

        @Serializable
        data object UserNotFound : Main



    }

    @Serializable
    data class ViewCropReport(
        val id : String
    ) : AppRouter


    @Serializable
    data class SurveyScreen(
        val id : String
    )
    @Serializable
    data class Forecast(
        val id : String
    )

}

















