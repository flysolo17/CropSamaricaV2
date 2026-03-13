package com.potatodevs.cropsamarica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.potatodevs.cropsamarica.ui.index.IndexScreen
import com.potatodevs.cropsamarica.ui.settings.LanguageViewModel
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val languageViewModel: LanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                languageViewModel.language.collect {
                    languageViewModel.applyLocale(it)

                }
            }
        }

        setContent {
            CropSamaricaTheme {
                IndexScreen()
            }
        }
    }
}
