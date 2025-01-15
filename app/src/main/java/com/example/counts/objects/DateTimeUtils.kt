package com.example.counts.objects

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateTimeUtils {

    private val calendar = Calendar.getInstance()

    private val FULL_FORMAT = "dd-mm-yyyy HH:mm:ss"
    private val AM_PM_FORMAT = "hh:mm a"
    private val MONTH_AS_TEXT_FORMAT = "dd-mm-yyyy"

    fun getFullDateAndTime(): String {
        val dateFormat = SimpleDateFormat(FULL_FORMAT, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


    fun timeAsAMPMFORMAT(): String {
        val AMPMFORMAT =
            SimpleDateFormat(AM_PM_FORMAT, Locale.getDefault()) // 12-hour format with AM/PM
        return AMPMFORMAT.format(calendar.time)
    }

    fun dateMonthAsTextFormat(): String {
        val MonthAsTextFormat = SimpleDateFormat(MONTH_AS_TEXT_FORMAT, Locale.getDefault())
        return MonthAsTextFormat.format(calendar.time)
    }

    fun convertOneFormatToAnother(dateTime: String, inputFormat: String, outputFormat: String) {

    }




}