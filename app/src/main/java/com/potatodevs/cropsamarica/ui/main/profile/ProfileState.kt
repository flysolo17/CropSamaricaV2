package com.potatodevs.cropsamarica.ui.main.profile

import com.potatodevs.cropsamarica.models.User

data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null
)