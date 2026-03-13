package com.potatodevs.cropsamarica.ui.main.common

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbar(
    onBack : () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    title : @Composable () -> Unit,
) {
    LargeTopAppBar(
        expandedHeight = 220.dp,
        title = title,
        navigationIcon = {
            IconButton(onClick = {
                onBack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = { /* …optional actions… */ },
        colors = TopAppBarDefaults.largeTopAppBarColors(

        ),
        scrollBehavior = scrollBehavior
    )
}