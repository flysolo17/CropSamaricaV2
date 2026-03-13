package com.potatodevs.cropsamarica.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.ui.config.AppRouter
import com.potatodevs.cropsamarica.ui.config.TOP_LEVEL_ROUTES
import com.potatodevs.cropsamarica.ui.main.router.Navigator
import com.potatodevs.cropsamarica.ui.main.router.mainFeatureSection
import com.potatodevs.cropsamarica.ui.main.router.rememberNavigationState
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uid : String,
    onCreateRiceFIeld: () -> Unit,
    onViewProfile : () -> Unit,
    onLogout : () -> Unit,
    onViewCropReport : (id : String) -> Unit,
    onNextStage : (id : String) -> Unit
) {
    val viewModel : MainViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    LaunchedEffect(uid) {
        events(MainEvents.GetRiceFields(uid))
    }

    MainScreen(
        mainViewModel = viewModel,
        onCreateRiceFIeld = onCreateRiceFIeld,
        onViewProfile = onViewProfile,
        user = state.user,
        selectedRiceField = state.selectedRiceField,
        isLoading = state.isLoading,
        riceFields = state.riceFields,
        onViewCropReport = onViewCropReport,
        onNextStage = onNextStage,
        mainEvents = events
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    onCreateRiceFIeld : () -> Unit,
    onViewProfile : () -> Unit,
    isLoading : Boolean,
    user: User?,
    selectedRiceField : RiceFieldWithRiceType?,
    riceFields : List<RiceFieldWithRiceType>,
    onViewCropReport: (id: String) -> Unit,
    onNextStage: (id: String) -> Unit,
    mainEvents: (MainEvents) -> Unit = {}
) {

    val navigationState = rememberNavigationState(
        startRoute = AppRouter.Main.Dashboard,
        topLevelRoutes = TOP_LEVEL_ROUTES.keys
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigator = remember { Navigator(navigationState) }
    fun onToggleDrawer() {
        scope.launch {
            drawerState.apply {
                if (isClosed) open() else close()
            }
        }
    }
    val entryProvider = entryProvider {
        mainFeatureSection(
            mainViewModel = mainViewModel,
            onCreateRiceField = onCreateRiceFIeld,
            onViewProfile = onViewProfile,
            onBack = { navigator.goBack() },
            onViewDetails = { navigator.navigate(AppRouter.Main.PestDetails(id = it)) },
            toggleDrawer = { onToggleDrawer() },
            onViewCropReport = onViewCropReport,
            onNextStage = onNextStage
        )
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.width(200.dp)
                    )
                }

                HorizontalDivider()
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    riceFields.forEach { item ->
                        val isSelected = item.riceField?.id == selectedRiceField?.riceField?.id

                        NavigationDrawerItem(
                            label = { Text(text =  "${item.riceField?.name}") },
                            selected = isSelected,
                            onClick = {
                                mainEvents(MainEvents.SelectRiceField(item))
                                onToggleDrawer()
                            }
                        )
                    }
                }

                NavigationDrawerItem(
                    label = { Text(text = "Create Rice Field", color = MaterialTheme.colorScheme.primary) },
                    selected = false,
                    icon = {
                        Icon(
                            tint = MaterialTheme.colorScheme.primary,
                            imageVector = androidx.compose.material.icons.Icons.Default.Add,
                            contentDescription = "Create Rice Field"
                        )
                    },
                    onClick = {
                        onToggleDrawer()
                        onCreateRiceFIeld()
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    NavigationBar {
                        TOP_LEVEL_ROUTES.forEach { (key, value) ->
                            val isSelected = key == navigationState.topLevelRoute
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { navigator.navigate(key) },
                                icon = {
                                    Icon(
                                        imageVector = value.icon,
                                        contentDescription = value.label
                                    )
                                },
                                label = { Text(value.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavDisplay(
                    entries = navigationState.toDecoratedEntries(entryProvider),
                    onBack = { navigator.goBack() }
                )
            }
        }
    }

}
