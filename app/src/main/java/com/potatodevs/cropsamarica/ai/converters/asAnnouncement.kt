package com.potatodevs.cropsamarica.ai.converters

import com.potatodevs.cropsamarica.models.announcement.Announcement
import com.potatodevs.cropsamarica.models.announcement.AnnouncementInfo
import com.potatodevs.cropsamarica.models.announcement.LocalizeAnnouncement
import com.potatodevs.cropsamarica.ui.utils.toDateOnly
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.Date

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
