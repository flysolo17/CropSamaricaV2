package com.potatodevs.cropsamarica.ai

import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.Schema
import com.potatodevs.cropsamarica.models.survey.QuestionType


const val SURVEY_GENERATION = "SURVEY_GENERATION"

private const val SURVEY_DESCRIPTION = """
    Generates a targeted diagnostic survey for farmers. 
    The goal is to gather specific data about the current crop state, environmental conditions, 
    and recent inputs to determine the optimal strategy for the next growth stage.
"""

val SURVEY_GENERATION_DECLARATION = FunctionDeclaration(
    name = SURVEY_GENERATION,
    description = SURVEY_DESCRIPTION,
    parameters = mapOf(
        "survey_goal" to Schema.string(description = "The purpose or goal of this survey"),
        "questions" to Schema.array(
            Schema.obj(
                mapOf(
                    "text" to Schema.string(description = "The question text"),
                    "type" to Schema.enumeration(
                        QuestionType.entries.map { it.name },
                        description = "Type of question"
                    ),
                    "options" to Schema.array(Schema.string(), description = "List of options for multiple choice or single choice questions. Leave empty for other types.", nullable = true)
                )
            )
        )
    )
)