package com.potatodevs.cropsamarica.repositories.ricefield

import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.potatodevs.cropsamarica.models.announcement.Announcement
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.rice.RiceType
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.weather.DailyForecast
import kotlinx.coroutines.flow.Flow

interface RiceFieldRepository {
    fun getAllByUid(uid: String): Flow<List<RiceFieldWithRiceType>>


    fun getRiceFieldWithId(
        riceFieldId: String,
    ) : Flow<RiceField>


   suspend fun deleteCropField(
        id: String
    ) : Result<String>

    suspend fun create(
        riceField : RiceField,
        selectedRiceType : RiceType,
        image : Uri? = null,
        dailyHighLow : List<String>  = emptyList()
    ) : Result<List<Task>>

    suspend fun getById(
        id : String
    ) : Result<RiceFieldWithRiceType>


    suspend fun generateAnnouncement(
        riceField: RiceFieldWithRiceType,
        forecast: DailyForecast
    ) : Result<Announcement>

}