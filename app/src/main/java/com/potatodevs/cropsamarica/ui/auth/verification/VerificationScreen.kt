package com.potatodevs.cropsamarica.ui.auth.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.PhoneAuthProvider
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme



@Composable
fun VerificationScreen(
    modifier: Modifier = Modifier,
    isVerifying : Boolean = false,
    timer : Long = 0L,
    phone : String,
    onVerify : (String) -> Unit = {},
    onResendCode : () -> Unit = {},
) {
    var otp by rememberSaveable {
        mutableStateOf("")
    }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    LaunchedEffect(otp) {
        if (otp.length == 6) {
            onVerify(otp)
        }
    }
    Scaffold(
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .imePadding(),
                enabled = !isVerifying && otp.length == 6,
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    if (otp.length == 6) {
                        onVerify(otp)
                    }
                }
            ) {
                Box(
                    modifier = Modifier.padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isVerifying) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text("Verify",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }


            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Verify phone", style = MaterialTheme.typography.headlineMedium)
            Text("Code has been sent to ${phone}")
            BasicTextField(
                singleLine = true,
                maxLines = 1,
                value = otp,
                modifier = Modifier.focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = {
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        otp = it
                    }
                },
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    repeat(6) { index ->
                        val char = when {
                            index >= otp.length -> ""
                            else -> otp[index].toString()
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val background = if (char.isEmpty()) {
                                MaterialTheme.colorScheme.surfaceVariant
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                            Text(char, style = MaterialTheme.typography.titleLarge)
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(2.dp)
                                    .background(
                                        color = background
                                    )
                            ) { }
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Text("Didn't receive a code?")
            TextButton(
                onClick = { onResendCode() },
                enabled = timer == 0L
            ) {
                val text = if (timer > 0) {
                    "${timer}s"
                } else {
                    "Resend Code"
                }
                Text(text = text)
            }
        }
    }

}

@Preview
@Composable
private fun VerificationScreenPrev() {
    CropSamaricaTheme {
        VerificationScreen(
            phone = "09123456789",
            onVerify = {},
            onResendCode = {},
            isVerifying = false,
            timer = 0L
        )
    }
}