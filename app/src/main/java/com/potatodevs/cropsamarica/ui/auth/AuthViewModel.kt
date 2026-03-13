package com.potatodevs.cropsamarica.ui.auth


import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.repositories.auth.AuthEventType

import com.potatodevs.cropsamarica.repositories.auth.AuthRepository
import com.potatodevs.cropsamarica.repositories.user.UserRepository
import com.potatodevs.cropsamarica.ui.config.AppRouter

import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



@HiltViewModel
class AuthViewModel @Inject constructor(
    private val  authRepository: AuthRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()


    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()


    private var timerJob: Job? = null

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        }
        override fun onVerificationFailed(e: FirebaseException) {
            viewModelScope.launch {
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        _oneTimeEvents.send(OneTimeEvents.ShowToast("Invalid code or credentials."))
                    }
                    is FirebaseTooManyRequestsException -> {
                        _oneTimeEvents.send(OneTimeEvents.ShowToast("Too many attempts. Please try again later."))
                    }
                    is FirebaseAuthMissingActivityForRecaptchaException -> {
                        _oneTimeEvents.send(OneTimeEvents.ShowToast("Recaptcha verification failed. Please restart the process."))
                    }
                    else -> {
                        _oneTimeEvents.send(OneTimeEvents.ShowToast(e.message ?: "Unexpected error occurred."))
                    }
                }
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            viewModelScope.launch {
                startTimer()
                _oneTimeEvents.send(OneTimeEvents.ShowToast("Code Sent!"))
                _state.update {
                    it.copy(
                        isLoading = false,
                        storedVerificationId = verificationId,
                        resendToken = token
                    )
                }

            }

        }
    }
    private fun startTimer(seconds: Long = 30) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remaining = seconds
            while (remaining > 0) {
                _state.update { it.copy(timer = remaining) }
                delay(1000L)
                remaining--
            }
            _state.update { it.copy(timer = 0L) }
        }
    }
    fun events(e : AuthEvents) {
        when (e) {
            is AuthEvents.OnSendCode -> {
                sendCode(type = e.type,e.activity, e.phone, callbacks)
            }
            is AuthEvents.OnCodeSent -> {
            }
            is AuthEvents.OnPhoneChange -> {
                _state.update {
                    when(e.type) {
                        AuthEventType.LOGIN -> it.copy(
                            loginState = it.loginState.copy(phone = e.phone)
                        )
                        AuthEventType.REGISTER -> it.copy(registerState = state.value.registerState.copy(phone = e.phone))

                    }
                }
            }

            is AuthEvents.RegisterEvents.OnBarangayChange -> {
                _state.update {
                    it.copy(
                        registerState = it.registerState.copy(
                            selectedBarangay = e.barangay
                        )
                    )
                }
            }
            is AuthEvents.RegisterEvents.OnMunicipalityChange -> {
                _state.update {
                    it.copy(
                        registerState = it.registerState.copy(
                            selectedMunicipality = e.municipality
                        )
                    )
                }
            }
            is AuthEvents.RegisterEvents.OnNameChange -> {
                _state.update {
                    it.copy(
                        registerState = it.registerState.copy(
                            name = e.name
                        )
                    )
                }
            }
            is AuthEvents.RegisterEvents.OnPhoneChange -> {
                _state.update {
                    it.copy(
                        registerState = it.registerState.copy(
                            phone = e.phone
                        )
                    )
                }
            }

            is AuthEvents.OnVerify -> {
                verify(e.type, e.otp, e.resendToken)
            }

            is AuthEvents.RegisterEvents.OnImageSelected -> {
                _state.update {
                    it.copy(
                        registerState = it.registerState.copy(
                            selectedImage = e.image
                        )
                    )
                }
            }
        }
    }

    private fun verify(
        type: AuthEventType,
        otp : String,
        resendToken: PhoneAuthProvider.ForceResendingToken?
    ) {
        viewModelScope.launch {
            if (_state.value.storedVerificationId == null) {
                _oneTimeEvents.send(OneTimeEvents.ShowToast("No verification id found"))
                return@launch
            }
            _state.update {
                it.copy(isLoading = true)
            }

            authRepository.verifyOtp(
                _state.value.storedVerificationId!!,
                otp,
                state.value.loginState.phone
            ).onSuccess {
                if (type == AuthEventType.REGISTER) {
                    val user = User(
                        id = it.user?.uid ?: "",
                        name = state.value.registerState.name,
                        phone = state.value.registerState.phone,
                        location = "${state.value.registerState.selectedMunicipality?.displayName}, ${state.value.registerState.selectedBarangay}"
                    )
                    userRepository.save(user,state.value.registerState.selectedImage)
                }
                _state.update { it.copy(
                    isLoading = false,
                    storedVerificationId = null,
                    resendToken = null,
                    loginState = LoginState(),
                    registerState = RegisterState()
                ) }
                val toastMessage = if (type == AuthEventType.LOGIN) "Logged in successfully" else "Registered successfully"
                _oneTimeEvents.send(OneTimeEvents.ShowToast(toastMessage))
                _oneTimeEvents.send(OneTimeEvents.Navigate(AppRouter.Main.Index(it.user?.uid ?: "")))

            }.onFailure {
                Log.d(
                    "AUTH",
                    "login: ${it.message}"
                )
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message ?: "Invalid OTP code"))
                _state.update { it.copy(
                   isLoading = false
                ) }
            }
        }
    }

    private fun sendCode(
        type : AuthEventType,
        activity: Activity,
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, phone = phoneNumber, authType = type) }
                authRepository.sendOtp(type = type,activity, phoneNumber, state.value.resendToken,callbacks)

            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _oneTimeEvents.send(OneTimeEvents.ShowToast(e.message ?: "Failed to send code"))
            }
        }
    }


}