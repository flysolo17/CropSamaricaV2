package com.potatodevs.cropsamarica.ui.auth

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.repositories.auth.AuthEventType
import com.potatodevs.cropsamarica.ui.auth.components.AuthTab
import com.potatodevs.cropsamarica.ui.auth.components.LoginPage
import com.potatodevs.cropsamarica.ui.auth.components.RegisterPage
import com.potatodevs.cropsamarica.ui.auth.verification.VerificationScreen

import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import kotlinx.coroutines.launch
import kotlin.concurrent.timer


@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    onNavigate : (Any) -> Unit,
    onBack : () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val oneTimeEvents = viewModel.oneTimeEvents
    val context = LocalContext.current
    LaunchedEffect(oneTimeEvents) {
        oneTimeEvents.collect { event ->
            when (event) {
                is OneTimeEvents.ShowToast -> {
                    Toast
                        .makeText(context, event.message, Toast.LENGTH_SHORT)
                        .show()
                }

                is OneTimeEvents.Navigate -> {
                    onNavigate(event.route)
                }
                OneTimeEvents.NavigateBack -> {
                    onBack()
                }
            }
        }
    }

    if (state.resendToken != null && state.storedVerificationId != null) {
        Dialog(
            onDismissRequest = {

            },
            properties = DialogProperties(
                decorFitsSystemWindows = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            VerificationScreen(
                isVerifying = state.isLoading,
                timer = state.timer,
                phone = state.phone,
                onVerify = {
                    if (state.authType == null) return@VerificationScreen
                    events(AuthEvents.OnVerify(type = state.authType!!,it, state.resendToken))
                },
                onResendCode = {
                    if (state.authType == null) return@VerificationScreen
                    events(AuthEvents.OnSendCode(
                        type = state.authType!!,
                        activity = context as Activity,
                        phone = state.phone
                    ))
                }
            )

        }
    }

    AuthScreen(
        modifier = modifier,
        loginState = state.loginState,
        isLoading = state.isLoading,
        registerState = state.registerState,
        events = events,

    )
    
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    loginState : LoginState,
    isLoading : Boolean,
    registerState : RegisterState,
    events : (AuthEvents) -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        2
    }

    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    modifier = Modifier.width(240.dp),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape =   RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        AuthTab(
                            currentPage = pagerState.currentPage,
                            selectedPage = pagerState.currentPage,
                            onTabSelected = { index ->
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { index ->
                            when(index) {
                                0 -> LoginPage(
                                    phone = loginState.phone,
                                    isLoggingIn = isLoading,
                                    onPhoneChange = {
                                        events(AuthEvents.OnPhoneChange(it, AuthEventType.LOGIN))
                                    },
                                    onLogin = { phone ->
                                        events(AuthEvents.OnSendCode(
                                            type = AuthEventType.LOGIN,
                                            activity = activity!!,
                                            phone = phone
                                        ))
                                    },
                                )
                                1 -> RegisterPage(
                                    isLoading = isLoading,
                                    state = registerState,
                                    events = { event -> events(event) },
                                    onRegister = {
                                        events(AuthEvents.OnSendCode(
                                            type = AuthEventType.REGISTER,
                                            activity = activity!!,
                                            phone = registerState.phone
                                        ))
                                    }
                                )

                            }
                        }

                    }

                }

            }
        }
    }
}

@Preview
@Composable
private fun AuthScreenPrev() {
    CropSamaricaTheme {
        AuthScreen(
            onNavigate = {},
            onBack = {}
        )
    }
}