package com.potatodevs.cropsamarica.ui.auth.components


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.auth.PhoneAuthProvider
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.Municipality
import com.potatodevs.cropsamarica.repositories.auth.AuthEventType
import com.potatodevs.cropsamarica.ui.auth.AuthEvents
import com.potatodevs.cropsamarica.ui.auth.AuthState
import com.potatodevs.cropsamarica.ui.auth.RegisterState
import com.potatodevs.cropsamarica.ui.main.home.components.ProfileImage
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme

@Composable
fun RegisterPage(
    modifier: Modifier = Modifier,
    isLoading : Boolean = false,
    state : RegisterState,

    events: (AuthEvents.RegisterEvents) -> Unit,
    onRegister : () -> Unit = {}
) {
    val width = Modifier.fillMaxWidth()
    val shape = MaterialTheme.shapes.medium
    val colors = TextFieldDefaults.colors()
    val name = state.name
    val isNameValid = name.isEmpty()
    val phone = state.phone
    val isPhoneValid = phone.isEmpty() && (phone.length == 10 && phone.startsWith("9"))
    var imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            events(AuthEvents.RegisterEvents.OnImageSelected(it))
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.outline,
                    CircleShape
                )
                .clickable { imageLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = state.selectedImage,
                contentDescription = "Profile Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.outline_add_a_photo_24),
                error = painterResource(R.drawable.outline_add_a_photo_24)
            )

        }
        OutlinedTextField(
            value = name,
            onValueChange = {
                events(AuthEvents.RegisterEvents.OnNameChange(it))
            },
            label = { Text("Fullname") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = width,
            shape = shape,
            colors = colors,
            isError = isNameValid,
            supportingText = {
                if (isNameValid) {
                    Text("Fullname is required", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error))
                }
            }
        )

        MunicipalitySelector(
            selectedMunicipality = state.selectedMunicipality,
            onMunicipalitySelected = { 
                events(AuthEvents.RegisterEvents.OnMunicipalityChange(it))
            },
        )
        BarangaySelector(
            selectedBarangay = state.selectedBarangay,
            barangays = state.selectedMunicipality?.barangays ?: emptyList(),
            onBarangaySelected = {
                events(AuthEvents.RegisterEvents.OnBarangayChange(it))
            }
        )

        val phone = state.phone

        OutlinedTextField(
            value = phone,
            onValueChange = {
                events(AuthEvents.RegisterEvents.OnPhoneChange(it))
            },
            label = { Text("Phone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
            shape = shape,
            modifier = width,
            isError = isPhoneValid,
            prefix = { Text("+63")},
            colors = colors,
            supportingText = {
                if (isPhoneValid) {
                    Text("Invalid Phone Number", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error))
                } else {
                    Text("Example: 9123456789", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
            },
        )




        Button(
            onClick = onRegister,
            modifier = Modifier.fillMaxWidth().padding(
                top = 12.dp
            ),
            enabled = !isNameValid && !isPhoneValid && !isLoading && state.selectedBarangay != null && state.selectedMunicipality != null && state.selectedBarangay.isNotEmpty(),
            shape = MaterialTheme.shapes.small,

        ) {
            Box(
                modifier = Modifier.padding(6.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Register", style = MaterialTheme.typography.titleMedium)
                }
            }

        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun RegisterPagePrev() {
    CropSamaricaTheme {
        RegisterPage(
            state = RegisterState(),
            events = {}
        )
    }
}