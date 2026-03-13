package com.potatodevs.cropsamarica.ui.main.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.ui.common.LoadingScreen
import com.potatodevs.cropsamarica.ui.errors.UserNotFound
import com.potatodevs.cropsamarica.ui.main.home.components.ProfileImage
import com.potatodevs.cropsamarica.ui.main.profile.components.EditProfile
import com.potatodevs.cropsamarica.ui.main.profile.components.ProfileButtons
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import com.potatodevs.cropsamarica.ui.utils.showToast


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onBack : () -> Unit,
    onViewDevelopers : () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout : () -> Unit,
    onViewUserGuide : () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val oneTimeEvents = viewModel.oneTimeEvents
    val events = viewModel::events
    val context = LocalContext.current
    LaunchedEffect(key1 = oneTimeEvents) {
        oneTimeEvents.collect {
            when (it) {
                is OneTimeEvents.Navigate -> {

                }
                OneTimeEvents.NavigateBack -> {
                }
                is OneTimeEvents.ShowToast -> {
                    if (it.message == "Logout Successful") {
                        onLogout()
                    }
                    context.showToast(it.message)
                }
            }
        }
    }

    when {
        state.isLoading -> {
            LoadingScreen(

            )
        }
        !state.isLoading && state.user == null -> {
            UserNotFound()
        }
        else -> {
            ProfileScreen(
                modifier = modifier,
                onViewDevelopers = onViewDevelopers,
                onBack = onBack,
                user = state.user,
                onViewUserGuide = onViewUserGuide,
                events = events,
            )
        }

    }
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onViewDevelopers : () -> Unit,
    onBack : () -> Unit,
    onViewUserGuide : () -> Unit,
    user: User?,
    events : (ProfileEvents) -> Unit,
) {
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) {
        if (it != null) {
            events(ProfileEvents.OnChangeProfile(it))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            val height = 250.dp
            val topBarImageBackground = painterResource(R.drawable.profile_bg)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(Color.Transparent)
                    .clip(
                        shape = RoundedCornerShape(
                            bottomStart = 32.dp,
                            bottomEnd = 32.dp
                        )
                    )
                ,
            ) {
                Image(
                    painter = topBarImageBackground,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.Black.copy(
                                alpha = 0.5f
                            )
                        )
                ) {

                }


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        .padding(
                            bottom = 16.dp
                        )
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {

                    IconButton(
                        modifier = Modifier.align(Alignment.Start),
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    ProfileImage(
                        name = user?.name.orEmpty(),
                        profile = user?.profile.orEmpty(),
                        imageSize = 56.dp,
                        onClick = {
                            imageLauncher.launch("image/*")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = user?.name.orEmpty().uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = user?.phone.orEmpty(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White.copy(
                                alpha = 0.7f
                            )
                        )
                    )
                }
            }

        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = stringResource(R.string.user_menu),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            EditProfile(
                currentName = user?.name.orEmpty(),
                onSaveChanges = { name, result ->
                    events(ProfileEvents.OnChangeName(name, result))
                }
            )

            Text(
                text = stringResource(R.string.others),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            ProfileButtons(
                icon = Icons.Filled.Book,
                title = stringResource(R.string.user_guide),
                onClick = {
                    onViewUserGuide()
                }
            )
            ProfileButtons(
                icon = Icons.Filled.Code,
                title = stringResource(R.string.developers),
                onClick = {
                    onViewDevelopers()

                }
            )
           ProfileButtons(
                icon = Icons.Filled.Settings,
                title = stringResource(R.string.settings),
                onClick = {

                }
            )
            Button(
                onClick = {
                    events(ProfileEvents.OnLogout)
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(text = stringResource(R.string.logout), modifier = Modifier.padding(8.dp))
            }
            Text("Crop Samarica 2025", style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.outline
            ))


        }

    }

}