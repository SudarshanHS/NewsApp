package com.example.newsapp.ui.utility


import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    fun getCurrentDate(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return currentDate.format(formatter)
        } else {
            return "2025-02-07"
        }
    }
}