package com.potatodevs.cropsamarica.ui.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangaySelector(
    modifier: Modifier = Modifier,
    barangays: List<String>,
    selectedBarangay: String? = null,
    onBarangaySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = selectedBarangay ?: "",
            onValueChange = {},
            readOnly = true,

            label = { Text("Barangay") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            modifier = modifier.fillMaxWidth().menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            barangays.forEach { barangay ->
                DropdownMenuItem(
                    text = { Text(barangay) },
                    onClick = {
                        onBarangaySelected(barangay)
                        expanded = false
                    }
                )
            }

        }
    }
}