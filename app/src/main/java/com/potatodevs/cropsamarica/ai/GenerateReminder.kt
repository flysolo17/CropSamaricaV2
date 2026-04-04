package com.potatodevs.cropsamarica.ai

import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.Schema


const val REMINDER = "reminder"

val CREATE_REMINDER = FunctionDeclaration(
    name = REMINDER,
    description = "Generates a list of reminders for the farmer to do based on the rice field data and weather forecast. The list will contain at least 2 distinct reminders.",
    parameters = mapOf(
        "reminders" to Schema.array(
            Schema.obj(
                mapOf(
                    "id" to Schema.string("A unique identifier for this reminder."),
                    "riceFieldId" to Schema.string("The ID of the specific rice field this reminder applies to."),
                    "stage" to Schema.string("The stage of the rice field at the time of the reminder."),
                    "message" to Schema.string("A short, clear, practical, and concise, farmer-friendly message detailing the reminder."),
                    "bestApplicationTime" to Schema.array(

                        description = "An hourly assessment of application suitability from 6 AM to 9 PM (21:00) for the reminderDate, based on the weather forecast. Each entry represents a specific hour within this window, indicating its suitability ('OPTIMAL', 'UNFAVORABLE', 'MODERATE'). There must be exactly one entry for each hour from 6 AM to 9 PM (16 entries total).",
                        items = Schema.obj(
                            mapOf(
                                "time" to Schema.string("The specific hour of the day, e.g., '6 AM', '7 AM', ..., '9 PM' (for 21:00)."),
                                "condition" to Schema.string("The condition indicating suitability for application at this hour. Must be one of 'OPTIMAL', 'UNFAVORABLE', or 'MODERATE'.")
                            )
                        ),
                        minItems = 16,
                        maxItems = 16
                    ),
                    "reminderDate" to Schema.string(
                        "The date of the reminder. Must always be between today and the next 6 days inclusive, based on the weather forecast. Format must be 'yyyy-MM-dd'."
                    )
                )
            )
        )
    ),
    optionalParameters = emptyList()
)