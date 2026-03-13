package com.potatodevs.cropsamarica.ui.utils

import com.potatodevs.cropsamarica.R
import com.potatodevs.cropsamarica.models.rice.RiceStage

import com.potatodevs.cropsamarica.models.rice.RiceType
import java.util.Date
import java.util.concurrent.TimeUnit


fun RiceStage.getIcon() : Int {
    return when (this) {
        RiceStage.SEEDLING -> R.drawable.seedling
        RiceStage.TILLERING -> R.drawable.tillering
        RiceStage.STEM_ELONGATION -> R.drawable.stem_elongation
        RiceStage.PANICLE_INITIATION -> R.drawable.panicle_initiation
        RiceStage.BOOTING -> R.drawable.booting
        RiceStage.FLOWERING -> R.drawable.flowering
        RiceStage.MILKING -> R.drawable.milking
        RiceStage.DOUGH -> R.drawable.dough
        RiceStage.MATURE -> R.drawable.mature
    }
}


fun Date.getRiceStage(): RiceStage {
    val currentDate = Date()
    val diffInMillis = currentDate.time - this.time
    val daysSincePlanting = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
    return when (daysSincePlanting) {
        in 0..14 -> RiceStage.SEEDLING
        in 15..30 -> RiceStage.TILLERING
        in 31..45 -> RiceStage.STEM_ELONGATION
        in 46..55 -> RiceStage.PANICLE_INITIATION
        in 56..65 -> RiceStage.BOOTING
        in 66..75 -> RiceStage.FLOWERING
        in 76..90 -> RiceStage.MILKING
        in 91..110 -> RiceStage.DOUGH
        in 111..120 -> RiceStage.MATURE
        else -> RiceStage.MATURE
    }
}

fun RiceStage.getHarvestDate(
    variety: RiceType?
): Date {
    val maturityRange = variety?.maturity ?: 110
    val harvestDate = Date()
    harvestDate.time += TimeUnit.DAYS.toMillis(maturityRange.toLong())
    return harvestDate
}
