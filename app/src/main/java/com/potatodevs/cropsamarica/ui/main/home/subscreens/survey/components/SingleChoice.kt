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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potatodevs.cropsamarica.models.survey.Question
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme

@Composable
fun SingleChoice(
    modifier: Modifier = Modifier,
    selectedAnswer : String,
    question: Question,
    onAnswerSelected: (String) -> Unit
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
            question.options?.forEachIndexed { index, string ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ){
                    RadioButton(
                        selected = selectedAnswer == string,
                        onClick = {
                            onAnswerSelected(string)
                        }
                    )
                    Text(
                        text = string,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
     
        }
    }
}


@Preview
@Composable
private fun SingleChoicePreview() {
    CropSamaricaTheme {
        SingleChoice(
            selectedAnswer = "Red",
            question = Question(
                text = "What is your favorite color?",
                options = listOf("Red", "Green", "Blue", "Yellow")
            ),
            onAnswerSelected = {}
        )
    }
}
