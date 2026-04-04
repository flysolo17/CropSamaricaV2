package com.potatodevs.cropsamarica.ui.main.crop_report.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.ui.main.crop_report.FarmerState
import com.potatodevs.cropsamarica.ui.main.home.components.ProfileImage

import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import com.potatodevs.cropsamarica.ui.theme.shimmer


@Composable
fun FarmerCard(
    modifier: Modifier = Modifier,
    farmer : FarmerState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(
                shimmering = farmer.isLoading,
                shape = MaterialTheme.shapes.medium
            ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Farmer Information", style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            ))
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage(
                    profile = farmer.user?.profile.orEmpty(),
                    imageSize = 60.dp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(farmer.user?.name.orEmpty(), style = MaterialTheme.typography.titleLarge)
                Text(farmer.user?.phone.orEmpty(), style = MaterialTheme.typography.bodyMedium)
            }


        }
    }

}

@Preview
@Composable
private fun FarmerCardPrev() {
    CropSamaricaTheme {
        FarmerCard(
            farmer = FarmerState()
        )
    }
}