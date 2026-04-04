package com.potatodevs.cropsamarica.ui.index

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.potatodevs.cropsamarica.ui.auth.AuthScreen
import com.potatodevs.cropsamarica.ui.auth.verification.VerificationScreen
import com.potatodevs.cropsamarica.ui.common.LoadingScreen
import com.potatodevs.cropsamarica.ui.config.AppRouter

import com.potatodevs.cropsamarica.ui.settings.LanguageSelectionScreen

import com.potatodevs.cropsamarica.ui.developer.DeveloperScreen
import com.potatodevs.cropsamarica.ui.errors.ErrorScreen
import com.potatodevs.cropsamarica.ui.errors.UserNotFound
import com.potatodevs.cropsamarica.ui.guide.GuideScreen
import com.potatodevs.cropsamarica.ui.main.MainScreen
import com.potatodevs.cropsamarica.ui.main.create_rice_field.CreateRiceFieldScreen
import com.potatodevs.cropsamarica.ui.main.forecast.ForecastScreen
import com.potatodevs.cropsamarica.ui.main.home.subscreens.survey.SurveyScreen
import com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop.ViewCropScreen
import com.potatodevs.cropsamarica.ui.main.pest.details.PestAndDiseaseDetailScreen
import com.potatodevs.cropsamarica.ui.main.profile.ProfileScreen
import com.potatodevs.cropsamarica.ui.notifications.NotificationScreen
import com.potatodevs.cropsamarica.ui.notifications.view.ViewNotificationScreen
import com.potatodevs.cropsamarica.ui.onboarding.OnboardingScreen
import com.potatodevs.cropsamarica.ui.settings.SettingsScreen
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import com.potatodevs.cropsamarica.ui.utils.showToast

@Composable
fun IndexScreen(
    modifier: Modifier = Modifier,
    viewModel: IndexViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val oneTimeEvents = viewModel.oneTimeEvents
    val context = LocalContext.current
    LaunchedEffect(oneTimeEvents) {
        oneTimeEvents.collect {
            when(it) {
                is OneTimeEvents.Navigate -> {

                }
                OneTimeEvents.NavigateBack -> {

                }
                is OneTimeEvents.ShowToast -> {
                    context.showToast(it.message)
                }
            }
        }
    }
    IndexScreen(
        isLoading = state.isLoading,
        uid = state.uid
    )

}
@Composable
fun IndexScreen(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    uid : String ?
) {
    val backStack = remember { mutableStateListOf<Any>(AppRouter.OnBoarding) }
    val currentDestination = backStack.lastOrNull()
    LaunchedEffect(isLoading,uid) {
        if (!isLoading && uid !== null) {
            backStack.add(AppRouter.Main.Index(uid))
        }
    }
    when {
        isLoading -> LoadingScreen()
        else -> {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = { key ->

                    when (key) {

                        is AppRouter.OnBoarding -> NavEntry(key) {
                            OnboardingScreen(
                                onSkip = {backStack.add(AppRouter.LanguageSelection)},
                                onStart = {backStack.add(AppRouter.LanguageSelection)}
                            )
                        }

                        is AppRouter.LanguageSelection -> NavEntry(key) {
                            LanguageSelectionScreen(
                                onNext = {
                                    backStack.add(AppRouter.Auth)
                                }
                            )
                        }


                        is AppRouter.Settings -> NavEntry(key) {
                            SettingsScreen (
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }


                        is AppRouter.Auth -> NavEntry(key) {
                            AuthScreen(
                                onNavigate = {
                                    backStack.add(it)
                                },
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }

                        is AppRouter.Notification -> NavEntry(key) {
                            NotificationScreen(
                                onViewNotification = {
                                    backStack.add(AppRouter.ViewNotification(it))
                                },
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                        is AppRouter.ViewNotification -> NavEntry(key) {
                            ViewNotificationScreen(
                                id = key.id,
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }


                        is AppRouter.Main.Index -> NavEntry(key) {
                            MainScreen(
                                navigateToNotification = {
                                    backStack.add(AppRouter.Notification)
                                },
                                uid = key.uid,
                                onCreateRiceFIeld = {
                                    backStack.add(AppRouter.CreateRiceField)
                                },
                                onViewProfile = {
                                    backStack.add(AppRouter.Main.Profile)
                                },
                                onLogout ={
                                    backStack.clear()
                                    backStack.add(AppRouter.OnBoarding)
                                },
                                onViewCropReport = {id : String ->
                                    backStack.add(AppRouter.ViewCropReport(id))
                                },
                                onViewPestDetails = { id: String ->
                                    backStack.add(AppRouter.Main.PestDetails(id))
                                },
                                onNextStage = { id : String ->
                                  backStack.add(AppRouter.SurveyScreen(id))
                                },
                                onWeatherClick = {
                                    backStack.add(AppRouter.Forecast(it))
                                }
                            )
                        }


                        is Error -> NavEntry(key) {
                            ErrorScreen(
                                onBack = { backStack.removeLastOrNull() }
                            )
                        }


                        is AppRouter.Main.Profile -> NavEntry(key) {
                            ProfileScreen(
                                onViewSettings = {
                                    backStack.add(AppRouter.Settings)
                                },
                                onViewDevelopers = {
                                    backStack.add(AppRouter.Main.Developer)
                                },
                                onBack = {
                                    backStack.removeLastOrNull()
                                },
                                onLogout = {
                                    backStack.clear()
                                    backStack.add(AppRouter.OnBoarding)
                                },
                                onViewUserGuide = {
                                    backStack.add(AppRouter.UserGuide)
                                }
                            )
                        }
                        is AppRouter.Main.UserNotFound -> NavEntry(key) {
                            UserNotFound()
                        }
                        is AppRouter.UserGuide -> NavEntry(key) {
                            GuideScreen(
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }

                        is AppRouter.CreateRiceField -> NavEntry(key) {
                            CreateRiceFieldScreen(
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }


                        is AppRouter.Main.Developer -> NavEntry(key) {
                            DeveloperScreen(
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                        is AppRouter.ViewCropReport -> NavEntry(key) {
                            ViewCropScreen(
                                id = key.id,
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                        is AppRouter.SurveyScreen -> NavEntry(key) {
                            SurveyScreen(
                                id = key.id,
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }

                        is AppRouter.Forecast -> NavEntry(key) {
                            ForecastScreen(
                                id = key.id,
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                        is AppRouter.Main.PestDetails -> NavEntry(key)  {
                            PestAndDiseaseDetailScreen(
                                id = key.id,
                                onBack = {
                                    backStack.removeLastOrNull()
                                },
                            )
                        }





                        else -> {
                            error("Unknown route: $key")
                        }
                    }
                }
            )
        }
    }
}


@Preview
@Composable
private fun IndexScreenPrev() {
    CropSamaricaTheme {
        IndexScreen()
    }
}