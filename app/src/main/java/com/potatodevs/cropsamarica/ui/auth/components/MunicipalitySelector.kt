package com.potatodevs.cropsamarica.ui.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.potatodevs.cropsamarica.models.Municipality
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MunicipalitySelector(
    modifier: Modifier = Modifier,
    selectedMunicipality: Municipality? = null,
    onMunicipalitySelected: (Municipality) -> Unit = {}
) {
    val municipalities = Municipality.entries

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            colors = TextFieldDefaults.colors(),
            shape = MaterialTheme.shapes.medium,
            value = selectedMunicipality?.displayName ?: "Select Municipality",
            onValueChange = {},
            readOnly = true,
            label = { Text("Municipality") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = modifier.fillMaxWidth().menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            municipalities.forEach { municipality ->
                DropdownMenuItem(
                    text = { Text(municipality.displayName) },
                    onClick = {
                        onMunicipalitySelected(municipality)
                        expanded = false
                    }
                )
            }

        }
    }
}

@Preview
@Composable
private fun MunicipalitySelectorPrev() {
    CropSamaricaTheme {
        MunicipalitySelector()
    }
}