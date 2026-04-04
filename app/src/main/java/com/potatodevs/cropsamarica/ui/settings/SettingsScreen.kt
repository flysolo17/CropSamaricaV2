package com.potatodevs.cropsamarica.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

import com.potatodevs.cropsamarica.restartApp
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val oneTimeEvents = viewModel.oneTimeEvents
    val context = LocalContext.current
    LaunchedEffect(key1 = oneTimeEvents) {
        oneTimeEvents.collect {
            when(it) {
                is OneTimeEvents.Navigate -> {
                    onBack()
                }
                OneTimeEvents.NavigateBack -> {
                   onBack()
                }
                is OneTimeEvents.ShowToast -> {
                    if (it.message == "change") {
                        restartApp(context = context)
                    }
                }
            }
        }
    }
    SettingsScreen(
        modifier = modifier,
        language = state.language,
        onBack = {
            onBack()
        },
        onChangeLanguage = {
            events(
                SettingsEvents.OnLanguageChanged(it)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    language : String = "en",
    onBack: () -> Unit,
    onChangeLanguage: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings")
                },
                navigationIcon = {
                    IconButton(onClick = { onBack()}) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = modifier.fillMaxSize().padding(it).padding(
                16.dp
            )
        ) {

            LanguageSelector(
                selectedLanguage = language,
                onLanguageSelected = {
                    onChangeLanguage(it)
                }
            )
        }

    }
}