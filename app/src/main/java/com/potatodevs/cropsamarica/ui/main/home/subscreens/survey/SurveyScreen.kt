package com.potatodevs.cropsamarica.ui.main.home.subscreens.survey

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.potatodevs.cropsamarica.models.survey.QuestionType
import com.potatodevs.cropsamarica.models.survey.Survey
import com.potatodevs.cropsamarica.ui.main.common.ImagePicker
import com.potatodevs.cropsamarica.ui.main.common.RecommendationDialog
import com.potatodevs.cropsamarica.ui.main.create_rice_field.CreateRiceFieldEvents
import com.potatodevs.cropsamarica.ui.main.home.subscreens.survey.components.LongAnswer
import com.potatodevs.cropsamarica.ui.main.home.subscreens.survey.components.MultipleChoice
import com.potatodevs.cropsamarica.ui.main.home.subscreens.survey.components.ShortAnswer
import com.potatodevs.cropsamarica.ui.main.home.subscreens.survey.components.SingleChoice
import com.potatodevs.cropsamarica.ui.main.home.subscreens.survey.components.TrueOrFalse
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import com.potatodevs.cropsamarica.ui.utils.showToast


@Composable
fun SurveyScreen(
    modifier: Modifier = Modifier,
    id: String,
    onBack: () -> Unit,
    viewModel: SurveyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    LaunchedEffect(id) {
        if (id.isNotEmpty()) {
            events(SurveyEvents.OnGenerateSurvey(id))
        }
    }

    val oneTimeEvents = viewModel.oneTimeEvents
    val context = LocalContext.current
    LaunchedEffect(oneTimeEvents) {
        oneTimeEvents.collect {
            when(it) {
                is OneTimeEvents.Navigate -> {
                    onBack()
                }
                OneTimeEvents.NavigateBack -> {
                    onBack()
                }
                is OneTimeEvents.ShowToast -> {
                    context.showToast(
                        it.message
                    )
                }
            }
        }

    }
    if (state.recommendations.isNotEmpty()) {
        RecommendationDialog(
            tasks = state.recommendations,
            onDismiss = {
                onBack()
            },
            onCreateTask = {
                events(SurveyEvents.OnSaveTasks(it))
            }
        )
    }
    SurveyScreen(
        id = id,
        modifier = modifier,
        isLoading = state.isLoading,
        survey = state.survey,
        onBack = onBack,
        selectedImages = state.selectedImage,
        questions = state.questionsWithAnswers,
        events = events
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    id : String,
    survey: QuestionState?,
    questions: List<QuestionWithAnswers> = emptyList(),
    selectedImages : List<Uri> = emptyList(),
    events: (SurveyEvents) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Survey")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(

            ) {
                Button(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    shape = MaterialTheme.shapes.medium,
                    enabled = !isLoading && survey?.isLoading == false,
                    onClick = {
                        events(SurveyEvents.OnSubmit)
                    }
                ) {
                    Box(
                        modifier = Modifier

                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(text = "Submit")
                        }
                    }

                }
            }
        },
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val generatingSurvey = survey?.isLoading
            if (generatingSurvey == true) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            if (survey?.error != null && generatingSurvey == false) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            12.dp,
                            Alignment.CenterVertically
                        )
                    ) {
                        Text("Something went wrong", style = MaterialTheme.typography.titleLarge)
                        Text(survey.error, style = MaterialTheme.typography.bodyMedium)
                        Button(onClick = {
                            events(SurveyEvents.OnGenerateSurvey(id))
                        }) {
                            Text("Retry")
                        }
                    }

                }
            }
            item {
                val purpose = survey?.survey?.purpose
                Text(text = purpose.orEmpty(), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center
                ))
            }


            itemsIndexed(
                items = questions,
                key = { index, _ -> index }
            ) { index, question ->
                when(question.question.type) {
                    QuestionType.MULTIPLE_CHOICE -> {
                        MultipleChoice(

                            question = question.question,
                            selectedAnswers = question.answer?.split(",") ?: emptyList(),
                            onAnswerSelected = {
                                events(SurveyEvents.OnChangeAnswer(
                                    index = index,
                                    answer = it
                                ))
                            }
                        )
                    }
                    QuestionType.SINGLE_CHOICE -> {
                        SingleChoice(
                            question = question.question,
                            selectedAnswer = question.answer.orEmpty(),
                            onAnswerSelected = {
                                events(SurveyEvents.OnChangeAnswer(
                                    index = index,
                                    answer = it
                                ))
                            }
                        )
                    }
                    QuestionType.SHORT_ANSWER -> {
                        ShortAnswer(
                            question = question.question,
                            answer = question.answer.orEmpty(),
                            onAnswerChanged = {
                                events(SurveyEvents.OnChangeAnswer(
                                    index = index,
                                    answer = it
                                ))
                            }
                        )
                    }
                    QuestionType.LONG_ANSWER -> {
                        LongAnswer(
                            question = question.question,
                            answer = question.answer.orEmpty(),
                            onAnswerChanged = {
                                events(SurveyEvents.OnChangeAnswer(
                                    index = index,
                                    answer = it
                                ))
                            }
                        )
                    }
                    QuestionType.TRUE_FALSE -> {
                        TrueOrFalse(
                            question = question.question,
                            answer = question.answer.toBoolean(),
                            onAnswerChanged = {
                                events(SurveyEvents.OnChangeAnswer(
                                    index = index,
                                    answer = it.toString()
                                ))
                            }
                        )
                    }

                    else -> {}
                }
            }
            if (survey?.isLoading == false && survey.error == null) {


                itemsIndexed(selectedImages, key = {index, _ -> index}) { index, uri ->
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth().padding(8.dp).height(
                            200.dp
                        ).clip(MaterialTheme.shapes.medium),
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    ImagePicker(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        selectedImageUri = null,
                        onImageSelected = {
                            events(SurveyEvents.OnImageChange(it))
                        }
                    )
                }
            }
        }
    }

}