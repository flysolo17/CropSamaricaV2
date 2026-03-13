package com.potatodevs.cropsamarica.ui.common

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import com.potatodevs.cropsamarica.ui.utils.convertMillisToDate
import com.potatodevs.cropsamarica.ui.utils.toDate
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiceStagePicker(
    modifier: Modifier = Modifier,
    selected: Date? = null,
    onSelected: (Date) -> Unit
) {

    var selectedDate by remember { mutableLongStateOf(selected?.time?: System.currentTimeMillis()) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate.convertMillisToDate(),
        onValueChange = { },
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(),
        label = { Text("Planted Date") },
        placeholder = { Text("Select Date") },
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },

        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {

                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                selectedDate = it?: System.currentTimeMillis()
                onSelected(selectedDate.toDate())
                showModal = false
            },
            onDismiss = { showModal = false },
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    minDate: Date? = null
) {
    val millisInDay = 24 * 60 * 60 * 1000L

    // If minDate is provided, disable dates before (minDate - 1 day), else default to 130 days ago
    val earliestAllowed = minDate?.time?.minus(millisInDay)
        ?: (System.currentTimeMillis() - (130L * millisInDay))

    val selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= earliestAllowed
        }

        override fun isSelectableYear(year: Int): Boolean {
            return true
        }
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = selectableDates,
        initialSelectedDateMillis = minDate?.time ?: System.currentTimeMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

