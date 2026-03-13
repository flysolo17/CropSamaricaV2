package com.potatodevs.cropsamarica.models.announcement

data class AnnouncementOld(
    val id : String = "",
    val title : String = "",
    val message : String = "",
    val fieldId : String = "",
    val date : String = "",
    val urgency : String = "",
    val createdAt : Long = System.currentTimeMillis(),
    val updatedAt : Long = System.currentTimeMillis()
)
