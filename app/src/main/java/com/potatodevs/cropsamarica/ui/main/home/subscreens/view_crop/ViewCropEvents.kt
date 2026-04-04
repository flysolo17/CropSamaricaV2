package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop

import androidx.lifecycle.ViewModel
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


sealed interface ViewCropEvents {
    data class GetRiceField(val riceFieldId : String) : ViewCropEvents
    data class OnStageSelected(val stage : RiceStage) : ViewCropEvents
    data class OnDeleteCrop(
        val id : String,
    ) : ViewCropEvents

}