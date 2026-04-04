package com.potatodevs.cropsamarica.repositories.notification

import com.potatodevs.cropsamarica.models.Notifications
import kotlinx.coroutines.flow.Flow

interface NotificationRepository

{
    fun getAllMyNotifications(
        uid : String
    ) : Flow<List<Notifications>>


    suspend fun getNotificationById(
        id : String
    ) : Result<Notifications?>
    suspend fun updateNotificationStatus(
        id : String,
    )

    suspend fun deleteNotification(
        id : String
    ) : Result<String>
}