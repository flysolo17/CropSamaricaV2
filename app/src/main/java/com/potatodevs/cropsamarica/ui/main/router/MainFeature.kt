package com.potatodevs.cropsamarica.ui.main.router

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.ui.config.AppRouter
import com.potatodevs.cropsamarica.ui.main.MainViewModel
import com.potatodevs.cropsamarica.ui.main.home.HomeScreen
import com.potatodevs.cropsamarica.ui.main.pest.PestScreen
import com.potatodevs.cropsamarica.ui.main.pest.details.PestAndDiseaseDetailScreen
import com.potatodevs.cropsamarica.ui.main.task.TaskScreen
import com.potatodevs.cropsamarica.ui.utils.showToast


fun EntryProviderScope<NavKey>.mainFeatureSection(
    mainViewModel: MainViewModel,
    onViewProfile: () -> Unit,
    onCreateRiceField: () -> Unit,
    onBack: () -> Unit,
    onViewDetails : (String) -> Unit,
    toggleDrawer : () -> Unit,
    onViewCropReport : (id : String) -> Unit,
    onNextStage : (id : String) -> Unit
) {
    entry<AppRouter.Main.Dashboard> {

        val state by mainViewModel.state.collectAsStateWithLifecycle()
        val context = LocalContext.current

        HomeScreen(
            navigateToCropReports = onViewCropReport,
            toggleDrawer = toggleDrawer,
            isLoading = state.isLoading,
            onViewProfile = onViewProfile,
            riceFields = state.riceFields,
            onCreateRiceField = onCreateRiceField,
            selectedRiceField = state.selectedRiceField,
            onNextStage = onNextStage
        )
    }
    entry<AppRouter.Main.PestAndDisease> {
        PestScreen(
            onViewDetails = {
                onViewDetails(it)
            }
        )
    }
    entry<AppRouter.Main.Task> { key ->
        TaskScreen()
    }

    entry<AppRouter.Main.PestDetails> { pestDetails ->
        PestAndDiseaseDetailScreen(
            id = pestDetails.id,
            onBack = onBack,
        )
    }

}