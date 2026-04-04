package com.potatodevs.cropsamarica.ui.config

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Task

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.Home

import androidx.navigation3.runtime.NavKey
import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.User
import kotlinx.serialization.Serializable



data class NavBarItem(

    @StringRes val label: Int,
    val icon: ImageVector,
    val  description : String = ""
)




val TOP_LEVEL_ROUTES = mapOf(
    AppRouter.Main.Dashboard to NavBarItem(
        icon = Icons.Default.Home,
        label = R.string.home
    ),
    AppRouter.Main.CropReport to NavBarItem(icon = Icons.Default.Newspaper, label = R.string.reports),
    AppRouter.Main.PestAndDisease to NavBarItem(icon = Icons.Default.BugReport, label =  R.string.pest_and_diseases),
    AppRouter.Main.Task to NavBarItem(icon = Icons.Default.Task, label =  R.string.task),
)
