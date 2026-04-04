package com.potatodevs.cropsamarica.ui.main.crop_report



sealed interface CropReportEvents {
    data class OnGetFertilizerTasks(
        val ids : List<String>
    ) : CropReportEvents
}