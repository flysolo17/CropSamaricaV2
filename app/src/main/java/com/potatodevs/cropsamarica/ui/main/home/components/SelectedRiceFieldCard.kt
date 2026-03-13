package com.potatodevs.cropsamarica.ui.main.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.rice.RiceField

@Composable
fun SelectedRiceFieldCard(
    modifier: Modifier = Modifier,
    selectedRiceField: RiceField?,
    onClick : () -> Unit
) {
    Card(
        modifier = modifier.clickable {
            onClick()
        },
        shape = MaterialTheme.shapes.extraLarge,

    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(selectedRiceField?.name ?: stringResource(R.string.no_field_selected), style = MaterialTheme.typography.titleSmall)
        }

    }

}