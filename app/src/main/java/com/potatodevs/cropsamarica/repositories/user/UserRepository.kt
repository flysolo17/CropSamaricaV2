package com.potatodevs.cropsamarica.repositories.user

import android.net.Uri
import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.ui.utils.UIState
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    suspend fun save(user : User,image : Uri ? = null) : Result<String>

    suspend fun getUser(
        id : String
    ) : Flow<User?>
    suspend fun changeName(
        uid : String,
        name : String,
        result : (UIState<String>) -> Unit
    )


    suspend fun uploadProfile(
        image : Uri
    ) : Result<String>

    suspend fun getFarmer() : Result<User?>
}