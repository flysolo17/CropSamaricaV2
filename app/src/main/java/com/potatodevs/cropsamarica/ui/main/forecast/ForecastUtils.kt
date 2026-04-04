package com.potatodevs.cropsamarica.ui.main.forecast

import com.potatodevs.cropsamarica.models.reminder.ApplicationCondition
import com.potatodevs.cropsamarica.models.reminder.BestApplicationTime
import com.potatodevs.cropsamarica.models.reminder.Reminder
import java.util.Date


    val REMINDERS = listOf(
        Reminder(
            id = "reminder_1",
            riceFieldId = "uePgWXgrkOVrxo086Keg",
            message = "Apply herbicide for early weed control during the morning window to avoid heavy rains and strong winds later in the day.",
            bestApplicationTime = listOf(
                BestApplicationTime("6 AM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("7 AM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("8 AM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("9 AM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("10 AM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("11 AM", ApplicationCondition.MODERATE),
                BestApplicationTime("12 PM", ApplicationCondition.MODERATE),
                BestApplicationTime("1 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("2 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("3 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("4 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("5 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("6 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("7 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("8 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("9 PM", ApplicationCondition.UNFAVORABLE)
            ),
            reminderDate = Date(1737302400000) // Mon Jan 20 2025
        ),
    Reminder(
        id = "reminder_2",
        riceFieldId = "uePgWXgrkOVrxo086Keg",
        message = "Inspect and clear your drainage canals and gates in the evening to prepare for or manage the persistent heavy rainfall.",
        bestApplicationTime= listOf(
            BestApplicationTime("6 AM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("7 AM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("8 AM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("9 AM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("10 AM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("11 AM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("12 PM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("1 PM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("2 PM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("3 PM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("4 PM", ApplicationCondition.UNFAVORABLE),
            BestApplicationTime("5 PM", ApplicationCondition.MODERATE),
            BestApplicationTime("6 PM", ApplicationCondition.OPTIMAL),
            BestApplicationTime("7 PM", ApplicationCondition.OPTIMAL),
            BestApplicationTime("8 PM", ApplicationCondition.OPTIMAL),
            BestApplicationTime("9 PM", ApplicationCondition.MODERATE)
        ),
        reminderDate = Date(1737388800000) // Tue Jan 21 2025
    )
)
