package com.potatodevs.cropsamarica.ai

import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.Schema
import com.potatodevs.cropsamarica.models.rice.RiceStage


val GENERATE_RECOMMENDATION = "GENERATE_RECOMMENDATION"
val STAGE_ENUM_NAMES = RiceStage.entries.joinToString(", ") { it.name }
val fertilizerReferenceTable = """
    FERTILIZER SCHEDULE REFERENCE:
    1. SEEDLING: 10 bags Organic, 14 Days Before Planting.
    2. SEEDLING: 2-4 kg Complete (14-14-14), 7-10 DAS.
    3. TILLERING: 2 sacks 14-14-14/16-20-0, 0-14 DAT or 10-14 DAS.
    4. TILLERING: 1 sack Urea (46-0-0), 18-22 DAT or 24-28 DAS.
    5. TILLERING: 1.5 sacks Urea/MOP, 28-32 DAT or 38-42 DAS.
""".trimIndent()
private val DESCRIPTION = """
    Generates agronomic recommendations and a detailed fertilizer schedule. 
    CRITICAL: For the 'stage' property, you MUST use one of these exact values: ${STAGE_ENUM_NAMES}.
    CRITICAL: For every item in the 'fertilizer_action_plan', you MUST calculate the 
    exact 'date' by applying the math formula P + offset (where P is 'Date Planted'). 
    Do not use the current date for tasks. Format all dates as 'MMMM d, yyyy'.
    Use the provided 'fertilizerReferenceTable' as the mandatory source of truth.
""".trimIndent()

val CREATE_RICE_FIELD_DECLARATION = FunctionDeclaration(
    name = GENERATE_RECOMMENDATION,
    description = DESCRIPTION,
    parameters = mapOf(
        "recommendations" to Schema.array(
            items = Schema.obj(
                properties = mapOf(
                    "title" to Schema.string(description = "A short title summarizing the recommendation."),
                    "details" to Schema.string(
                        nullable = true,
                        description = "Practical, farmer-friendly explanation. Max 2 sentences."
                    )
                )
            )
        ),
        "fertilizer_action_plan" to Schema.array(
            items = Schema.obj(
                properties = mapOf(
                    "title" to Schema.string(description = "Action title, e.g., 'Apply 2 sacks of 14-14-14'."),
                    "stage" to Schema.string(description = "The corresponding RiceStage enum name (e.g., SEEDLING, TILLERING)."),
                    "type" to Schema.string(description = "The specific fertilizer name/type."),
                    "date" to Schema.string(description = "Calculated date based on planting start date and the scheduled timing."),
                    "purpose" to Schema.string(
                        nullable = true,
                        description = "The goal of this application, e.g., 'Promote tiller formation'."
                    )
                ),
            ),
            description = "A full list of all required fertilizer applications based on the rice growth stages."
        )
    )
)


