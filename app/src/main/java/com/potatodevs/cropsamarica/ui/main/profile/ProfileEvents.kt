package com.potatodevs.cropsamarica.ui.main.profile

import android.net.Uri
import com.potatodevs.cropsamarica.ui.utils.UIState

sealed interface ProfileEvents{
    object OnLogout : ProfileEvents

    data class OnChangeName(val name : String,val result : (UIState<String>) -> Unit) : ProfileEvents

    data class OnChangeProfile(val uri : Uri) : ProfileEvents
}