package com.potatodevs.cropsamarica.ui.config

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Task

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.navigation3.runtime.NavKey
import com.potatodevs.cropsamarica.models.User
import kotlinx.serialization.Serializable


@Serializable
sealed interface MainRoute : NavKey {

}

data class NavBarItem(

    val label: String,
    val icon: ImageVector,
    val  description : String = ""
)




val TOP_LEVEL_ROUTES = mapOf(
    AppRouter.Main.Dashboard to NavBarItem(
        icon = Icons.Default.Home,
        label = "Home"
    ),
    AppRouter.Main.PestAndDisease to NavBarItem(icon = Icons.Default.BugReport, label = "Pest And Disease"),
    AppRouter.Main.Task to NavBarItem(icon = Icons.Default.Task, label = "Tasks"),
)
