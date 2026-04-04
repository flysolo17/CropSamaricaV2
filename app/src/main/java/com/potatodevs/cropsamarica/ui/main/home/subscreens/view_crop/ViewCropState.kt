package com.potatodevs.cropsamarica.ui.main.home.subscreens.view_crop

import com.potatodevs.cropsamarica.models.User
import com.potatodevs.cropsamarica.models.reminder.Reminder
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceFieldWithRiceType
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.tasks.Task


data class ViewCropState(
    val isLoading : Boolean = false,
    val riceField : RiceFieldWithRiceType? = null,
    val selectedTab : RiceStage = RiceStage.SEEDLING,

)

