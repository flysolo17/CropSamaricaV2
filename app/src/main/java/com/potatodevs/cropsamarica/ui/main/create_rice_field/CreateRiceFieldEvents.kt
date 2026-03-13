package com.potatodevs.cropsamarica.ui.main.create_rice_field

import android.net.Uri
import com.potatodevs.cropsamarica.models.Municipality
import com.potatodevs.cropsamarica.models.rice.IrrigationType
import com.potatodevs.cropsamarica.models.rice.RiceType
import com.potatodevs.cropsamarica.models.rice.SoilTypes
import com.potatodevs.cropsamarica.models.tasks.Task
import java.util.Date

sealed interface CreateRiceFieldEvents {
    data object Submit : CreateRiceFieldEvents

    data class OnNameChange(
        val name: String
    ) : CreateRiceFieldEvents
    data class OnMunicipalityChange(
        val municipality: Municipality
    ) : CreateRiceFieldEvents
    data class OnBarangayChange(
        val barangay: String
    ) : CreateRiceFieldEvents

    data class OnAreaChange(
        val area: String
    ) : CreateRiceFieldEvents
    data class OnPlantedDateChange(
        val plantedDate: Date
    ) : CreateRiceFieldEvents
    data class OnVarietyChange(
        val variety: RiceType
    ) : CreateRiceFieldEvents

    data class OnSoilTypeChange(
        val soilType: SoilTypes
    ) : CreateRiceFieldEvents

    data class OnIrrigationTypeChange(
        val irrigationType: IrrigationType
    ) : CreateRiceFieldEvents

    data class OnImageChange(
        val image: Uri
    ) : CreateRiceFieldEvents

    data class OnGetWeather(
        val location: String
    ) : CreateRiceFieldEvents


    data class OnCreateTask(
        val tasks: List<Task>
    ) : CreateRiceFieldEvents

}
