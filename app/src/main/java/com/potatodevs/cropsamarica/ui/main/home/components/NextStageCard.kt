package com.potatodevs.cropsamarica.ui.main.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import com.potatodevs.cropsamarica.ui.theme.shimmer

@Composable
fun NextStageCard(
    modifier: Modifier = Modifier,
    stage: RiceStage,
    isLoading: Boolean = false,
    onNextStage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shimmer(
                shimmering = isLoading,
                shape = MaterialTheme.shapes.large
            )
        ,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                stringResource(R.string.next_stage), style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ))
            Text(stringResource(R.string.your_crop_is_ready_for_next_stage), style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(
            onClick = onNextStage,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next Stage"
            )
        }

    }

}

@Preview
@Composable
private fun NextStagePrev() {
    CropSamaricaTheme {
        NextStageCard(
            stage = RiceStage.SEEDLING,
            onNextStage = {}
        )
    }

}