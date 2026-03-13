package com.potatodevs.cropsamarica.ui.auth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.PhoneAuthProvider
import com.potatodevs.cropsamarica.repositories.auth.AuthEventType
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    phone: String,
    onPhoneChange: (String) -> Unit,
    isLoggingIn : Boolean = false,
    onLogin : (phone : String) -> Unit
) {

    val width = Modifier.fillMaxWidth()
    val shape = MaterialTheme.shapes.medium
    val colors = TextFieldDefaults.colors()
    var isPhoneTouch by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().onFocusChanged {
                if (it.isFocused) {
                    isPhoneTouch = true
                }
            },
            shape = MaterialTheme.shapes.medium,
            value = phone,

            maxLines = 1,
            onValueChange = {
                onPhoneChange(it)
            },
            prefix = {
                if (isPhoneTouch) {
                    Text("+63")
                }
            },
            label = { Text("Enter phone number") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(),
            singleLine = true,
            isError = isPhoneTouch && phone.length != 10 && !phone.startsWith("9"),
            supportingText = {
                if (isPhoneTouch && (phone.length != 10 || !phone.startsWith("9"))) {
                    Text("Invalid Phone Number", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error))
                } else {
                    Text("Example: 9123456789", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
            }
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Button(
            onClick = { onLogin(phone) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled =!isLoggingIn
        ) {
            Box(modifier = Modifier.padding(6.dp)) {
                if (isLoggingIn) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginPagePrev() {
    CropSamaricaTheme {
        LoginPage(
            phone = "",
            onPhoneChange = {},
            isLoggingIn = false,
            onLogin = { phone ->
            },
        )
    }
}