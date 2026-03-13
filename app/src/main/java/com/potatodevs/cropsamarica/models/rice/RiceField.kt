package com.potatodevs.cropsamarica.models.rice


import java.util.Date

data class RiceField(
    var id: String = "",
    var uid: String = "",
    var image : String  = "",
    val name: String = "",
    val stage: RiceStage = RiceStage.TILLERING,
    var location: String = "",
    val plantedDate: Long = System.currentTimeMillis(),
    val expectedHarvestDate: Long? = null,
    val variety: String = "",
    val status: RiceStatus = RiceStatus.EXCELLENT,
    val areaSize: Double = 0.0,
    val irrigationType: IrrigationType = IrrigationType.NONE,
    val soilType : SoilTypes = SoilTypes.CLAY,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
)


fun RiceField.getYield(type: RiceType)
: Double {
    return this.areaSize * type.yieldPerHectare
}