package com.potatodevs.cropsamarica.models.fertilizer

import com.potatodevs.cropsamarica.models.rice.RiceStage

data class Fertilizer(
    val stage: RiceStage,
    val type: String,
    val amount: String,
    val timing: String,
    val purpose: String
)



val fertilizerList = listOf(
    Fertilizer(
        stage = RiceStage.SEEDLING,
        type = "Organic Fertilizer",
        amount = "10 bags",
        timing = "14 Days Before Planting",
        purpose = "To ensure early, accessible nutrient availability, fostering strong root development and maximizing initial growth"
    ),
    Fertilizer(
        stage = RiceStage.SEEDLING,
        type = "Complete Fertilizer (14-14-14)",
        amount = "2-4 kg",
        timing = "7-10 DAS",
        purpose = "To produce healthy seedlings"
    ),
    Fertilizer(
        stage = RiceStage.TILLERING,
        type = "14-14-14 or 16-20-0",
        amount = "2 sacks",
        timing = "0-14 DAT or 10-14 DAS",
        purpose = "Supplies nutrients that promote tiller formation and vigorous vegetative growth, increasing the number of productive stems."
    ),
    Fertilizer(
        stage = RiceStage.TILLERING,
        type = "Urea (46-0-0)",
        amount = "1 sack",
        timing = "18-22 DAT or 24-28 DAS",
        purpose = "Supplies nitrogen to support rapid stem growth, leaf development, and panicle formation."
    ),
    Fertilizer(
        stage = RiceStage.TILLERING,
        type = "Urea (46-0-0) and Muriate of Potash (0-0-60)",
        amount = "1 sack and 0.5 sack",
        timing = "28-32 DAT or 38-42 DAS (plus/minus 5 days)",
        purpose = "Nitrogen supports grain development, while potassium improves pollination success and stress resistance."
    ),
    Fertilizer(
        stage = RiceStage.MATURE,
        type = "N/A",
        amount = "None",
        timing = "Harvest phase",
        purpose = "Harvest-ready, fertilizer complete"
    )
)