package com.tudomart.store.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

//fun Date.isYesterday(): Boolean = DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
fun Date.isTomorrow(): Boolean = DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)

fun Date.isToday(): Boolean = DateUtils.isToday(this.time)


fun Date.toDateString(): String {
    return try {
        val sdf = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
        when {
            this.isToday() -> {
                "Today"
            }
            this.isTomorrow() -> {
                "Tomorrow"
            }
            else -> {
                sdf.format(this)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

fun String.toDateString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return sdf.parse(this)?.toDateString() ?: this
}