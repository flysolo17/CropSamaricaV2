package com.potatodevs.cropsamarica.ui.utils

import androidx.compose.ui.graphics.Color
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.tasks.TaskStatus
import java.util.Date


fun Task.isStartingToday() : Boolean {
    val today = Date()
    val taskDate = this.startDate
    return today.day == taskDate?.day && today.month == taskDate.month && today.year == taskDate.year
}

fun Date.isDueToday() : Boolean {
    val today = Date()
    val dueDate = this
    return today.day == dueDate.day && today.month == dueDate.month && today.year == dueDate.year
}

fun TaskStatus.getBackgroundColor(): Color {
    return when (this) {
        TaskStatus.PENDING -> Color(0xFFFFCDD2)
        TaskStatus.IN_PROGRESS -> Color(0xFFFFF9C4)
        TaskStatus.COMPLETED -> Color(0xFFC8E6C9)
    }
}

fun TaskStatus.getTextColor(): Color {
    return when (this) {
        TaskStatus.PENDING -> Color(0xFFB71C1C)   // Dark Red
        TaskStatus.IN_PROGRESS -> Color(0xFFF57F17) // Dark Yellow/Amber
        TaskStatus.COMPLETED -> Color(0xFF1B5E20) // Dark Green
    }
}

fun Task.getBackgroundColor(): Color {
    val now = Date()

    return when {

        status == TaskStatus.COMPLETED -> Color(0xFFC8E6C9)


        dueDate != null && dueDate.before(now) -> Color(0xFFFFCDD2)


        startDate != null && dueDate == null && startDate.isDueToday() -> Color(0xFFFFF9C4)


        startDate != null && startDate.after(now) -> Color(0xFFE3F2FD)


        else -> status.getBackgroundColor()
    }
}


fun Task.getTextColor(): Color {
    val now = Date()

    return when {
        // ✅ Completed task (always green text)
        status == TaskStatus.COMPLETED -> Color(0xFF388E3C)

        // Overdue task (past due date, not completed)
        dueDate != null && dueDate.before(now) -> Color(0xFFB71C1C)

        // Task starts today (no due date)
        startDate != null && dueDate == null && startDate.isDueToday() -> Color(0xFFF57F17)

        // Upcoming task (start date is in the future)
        startDate != null && startDate.after(now) -> Color(0xFF1976D2)

        // Fallback: status-based color
        else -> status.getTextColor()
    }
}

fun Task?.getStartAndDue(): String? {
    if (this == null) return null

    val start = this.startDate?.monthAndDay()
    val due = this.dueDate?.monthAndDay()
    return when {
        start == null && due == null -> null
        start == due -> start
        start == null -> due
        due == null -> start
        else -> "$start - $due"
    }
}
