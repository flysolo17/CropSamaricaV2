package com.potatodevs.cropsamarica.ui.developer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.ui.main.home.components.ProfileImage
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperScreen(modifier: Modifier = Modifier,onBack : () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Developers") },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier.padding(it),
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(DEVELOPERS, key = { it.name }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(it.profile),
                            contentDescription = it.name,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = it.role,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = it.email,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

        }
    }
}


@Preview
@Composable
private fun DeveloperScreenPrev() {
    CropSamaricaTheme {

        DeveloperScreen()
    }
}