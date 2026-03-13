package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop

import androidx.lifecycle.ViewModel
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


sealed interface ViewCropEvents {
    data class OnGetCrop(val id : String) : ViewCropEvents

    data object OnGetFarmer : ViewCropEvents

    data class OnGetTasks(val id : String) : ViewCropEvents

}