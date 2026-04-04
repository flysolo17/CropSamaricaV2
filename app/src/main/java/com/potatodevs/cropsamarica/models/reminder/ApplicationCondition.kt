package com.potatodevs.cropsamarica.models.reminder

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

enum class ApplicationCondition(
    val displayName : String,
    val image : ImageVector,
    val color : androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified
) {
    OPTIMAL(
       displayName =  "Optimal",
        image = Icons.Filled.Check,
        color = androidx.compose.ui.graphics.Color.Green
    ),
    UNFAVORABLE(
       displayName =  "Unfavorable",
       image= Icons.Filled.Close,
        color = androidx.compose.ui.graphics.Color.Red
    ),

    MODERATE(
        displayName = "Moderate",
        image = Icons.Filled.Warning,
        color = androidx.compose.ui.graphics.Color.Yellow
    )
}