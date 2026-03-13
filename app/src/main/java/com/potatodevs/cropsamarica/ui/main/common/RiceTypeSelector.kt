package com.potatodevs.cropsamarica.ui.main.common

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
import com.potatodevs.cropsamarica.models.rice.RiceType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiceTypeSelector(
    modifier: Modifier = Modifier,
    selectedRiceType : RiceType? = null,
    onRiceTypeSelected : (RiceType) -> Unit,
    riceTypes : List<RiceType>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            value = selectedRiceType?.name ?: "",
            onValueChange = {},
            label = {
                Text("Select Rice Type")
            },
            readOnly = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            riceTypes.forEach { riceType ->
                DropdownMenuItem(
                    text = { Text(text = riceType.name)
                },
                    onClick = {
                        onRiceTypeSelected(riceType)
                        expanded = false
                    }
                )
            }
        }
    }

}