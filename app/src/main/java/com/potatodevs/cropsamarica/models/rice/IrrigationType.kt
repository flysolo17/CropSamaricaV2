package com.potatodevs.cropsamarica.models.rice

enum class IrrigationType(
    val displayName: String
){
    RAINFED("Rainfed"),
    GRAVITY_IRRIGATION("Gravity Irrigation"),
    PUMP_IRRIGATION("Pump Irrigation"),
    NONE("None"),
    SUPPLEMENTAL("Supplemental"),
}