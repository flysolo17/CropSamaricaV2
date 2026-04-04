package com.potatodevs.cropsamarica.ui.main.home.subscreens.survey.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.models.survey.Question
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme

@Composable
fun ShortAnswer(
    modifier: Modifier = Modifier,
    answer: String,
    question: Question,
    onAnswerChanged: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = question.text ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = answer,
                onValueChange = onAnswerChanged,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Answer")
                }
            )
        }
    }
}


@Preview
@Composable
private fun ShortAnswerPreview() {
    CropSamaricaTheme {
        ShortAnswer(
            answer = "Red",
            question = Question(
                text = "What is your favorite color?"
            ),
            onAnswerChanged = {}
        )
    }
}


@Composable
fun LongAnswer(
    modifier: Modifier = Modifier,
    answer: String,
    question: Question,
    onAnswerChanged: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = question.text ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = answer,
                onValueChange = onAnswerChanged,
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                label = {
                    Text(text = "Answer")
                }
            )
        }
    }
}


@Preview
@Composable
private fun LongAnswerPreview() {
    CropSamaricaTheme {
        LongAnswer(
            answer = "Red",
            question = Question(
                text = "What is your favorite color?"
            ),
            onAnswerChanged = {}
        )
    }
}


