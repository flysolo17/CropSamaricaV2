package com.potatodevs.cropsamarica.ui.utils

import android.content.Context
import android.widget.Toast


const val USERS_COLLECTION = "users";
const val VARIETIES_COLLECTION = "varieties";
const val RICE_FIELD_COLLECTION = "rice_fields";

const val PESTS_COLLECTION = "pest_and_diseases";

const val TASKS_COLLECTION = "tasks"
const val REMINDERS_COLLECTION  = "reminders"


fun Context.showToast(
     message : String,
     duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this,message,duration).show()
}