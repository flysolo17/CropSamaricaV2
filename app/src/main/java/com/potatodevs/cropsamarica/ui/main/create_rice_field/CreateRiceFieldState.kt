package com.potatodevs.cropsamarica.ui.main.create_rice_field

import android.net.Uri
import com.potatodevs.cropsamarica.models.Municipality
import com.potatodevs.cropsamarica.models.rice.IrrigationType
import com.potatodevs.cropsamarica.models.rice.RiceType
import com.potatodevs.cropsamarica.models.rice.SoilTypes
import com.potatodevs.cropsamarica.models.tasks.Task
import java.util.Date


data class CreateRiceFieldState(
    val name : String = "",
    val isLoading : Boolean = false,
    val municipality : Municipality ? = null,
    val barangay : String = "",
    val plantedDate : Date ? = null,
    val areaSize : String = "",
    val variety : RiceType ? = null,
    val soilTypes: SoilTypes ? = null,
    val irrigationType: IrrigationType = IrrigationType.GRAVITY_IRRIGATION,
    val riceTypes : List<RiceType> = emptyList(),
    val selectedImage : Uri ? = null,
    val dailyHighLow : List<String> = emptyList(),
    val recommendations : List<Task> = emptyList()
)