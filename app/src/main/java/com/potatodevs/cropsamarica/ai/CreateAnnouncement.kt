package com.potatodevs.cropsamarica.ai

import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.Schema


const val ANOUNCEMENT = "announcement"
val CREATE_ANNOUNCEMENT = FunctionDeclaration(
    name = ANOUNCEMENT,
    description = "Generates one clear and practical announcement for rice farmers based on the rice field data, weather forecast, and tasks. (Supports English and Tagalog languages)",
    parameters = mapOf(
        "announcement" to Schema.obj(
            mapOf(
                "en" to Schema.obj(
                    mapOf(
                        "title" to Schema.string("A short title for the announcement (English)."),
                        "message" to Schema.string("A short, clear, practical, and concise, farmer-friendly message (English)."),
                        "urgency" to Schema.enumeration(
                            listOf("LOW", "MEDIUM", "HIGH"),
                            "Level of urgency for this announcement (English)."
                        )
                    )
                ),
                "tl" to Schema.obj(
                    mapOf(
                        "title" to Schema.string("Maikling pamagat para sa anunsyo (Tagalog)."),
                        "message" to Schema.string("Maikli, malinaw, at praktikal na mensahe na madaling maintindihan ng mga magsasaka (Tagalog)."),
                        "urgency" to Schema.enumeration(
                            listOf("LOW", "MEDIUM", "HIGH"),
                            "Antas ng kahalagahan para sa anunsyong ito (Tagalog)."
                        )
                    )
                )
            )
        )
    )
)