package com.potatodevs.cropsamarica.models.announcement


import com.potatodevs.cropsamarica.ui.utils.toDateOnly
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.Date

data class LocalizeAnnouncement(
    val en : AnnouncementInfo = AnnouncementInfo(),
    val tl : AnnouncementInfo = AnnouncementInfo(),
)

data class AnnouncementInfo(
    val title : String = "",
    val message : String = "",
    val urgency : String = "",

)

data class Announcement(
    val id : String = "",
    val information : LocalizeAnnouncement = LocalizeAnnouncement(),
    val fieldId : String = "",
    val uid : String = "",
    val date : String = "",
    val createdAt : Long = System.currentTimeMillis(),
    val updatedAt : Long = System.currentTimeMillis()
)

fun Map<String, JsonElement>.asAnnouncement(fieldId: String): Announcement {
    val now = System.currentTimeMillis()

    fun getInfo(locale: String): AnnouncementInfo {
        val localeMap = this[locale]?.jsonObject
        val title = localeMap?.get("title")?.jsonPrimitive?.contentOrNull ?: ""
        val message = localeMap?.get("message")?.jsonPrimitive?.contentOrNull ?: ""
        val urgency = localeMap?.get("urgency")?.jsonPrimitive?.contentOrNull ?: "LOW"
        return AnnouncementInfo(title, message, urgency)
    }

    val enInfo = getInfo("en")
    val tlInfo = getInfo("tl").takeIf {
        it.title.isNotBlank() || it.message.isNotBlank()
    } ?: enInfo

    return Announcement(
        id = "",
        information = LocalizeAnnouncement(en = enInfo, tl = tlInfo),
        fieldId = fieldId,
        date = Date().toDateOnly(),
        createdAt = now,
        updatedAt = now
    )
}

