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
import androidx.compose.ui.tooling.preview.Preview
import com.potatodevs.cropsamarica.models.rice.IrrigationType
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IrrigationSelector(
    modifier: Modifier = Modifier,
    selected: IrrigationType,
    onSelected: (IrrigationType) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected.name,
            onValueChange = {},
            colors = TextFieldDefaults.colors(),
            shape = MaterialTheme.shapes.medium,
            readOnly = true,
            label = { Text("Irrigation Type") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            IrrigationType.entries.forEach {
                DropdownMenuItem(
                    text = { Text(it.displayName) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }


}


@Preview(
    showBackground = true,
)
@Composable
private fun IrrigationSelectorPreview() {
    CropSamaricaTheme {
        IrrigationSelector(
            selected = IrrigationType.RAINFED,
            onSelected = {}
        )
    }
}