package com.potatodevs.cropsamarica.ui.main.home.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.R

@Composable
fun NoRiceFieldContent(
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit,
    onCreate : () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier.align(Alignment.End)
        ) {
            content()
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.rice),
                contentDescription = "No rice fields found",
                modifier = Modifier.padding(vertical = 8.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Text(
                stringResource(R.string.no_rice_fields_found),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = stringResource(R.string.create_a_new_rice_field_to_get_started),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            BouncingArrow()
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCreate,
                modifier = Modifier.padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.create))
                }
            }
        }

    }

}

@Composable
fun BouncingArrow() {
    val infiniteTransition = rememberInfiniteTransition(label = "arrowBounce")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounceOffset"
    )
    Icon(
        imageVector = Icons.Default.ArrowDownward,
        contentDescription = "Bouncing Arrow",
        modifier = Modifier
            .size(48.dp)
            .offset(y = offsetY.dp), // convert Float → Dp here
        tint = MaterialTheme.colorScheme.primary
    )
}