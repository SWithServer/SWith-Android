package com.example.swith.utils

import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.ZoneId
import java.time.ZonedDateTime

fun compareTimeWithNow(timeList: List<Int>) : Boolean{
    with(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))) {
        val nowTimeToLong = String.format("%4d%02d%02d%02d%02d", year, monthValue, dayOfMonth, hour, minute).toLong()
        val sessionTimeToLong = String.format("%4d%02d%02d%02d%02d", timeList[0], timeList[1], timeList[2], timeList[3], timeList[4]).toLong()
        if (sessionTimeToLong < nowTimeToLong) return false
    }
    return true
}

fun compareDayWithNow(date: CalendarDay) : Boolean{
    with(ZonedDateTime.now(ZoneId.of("Asia/Seoul"))) {
        val nowToLong = String.format("%4d%02d%02d", year, monthValue, dayOfMonth).toLong()
        val dayToLong = String.format("%4d%02d%02d", date.year, date.month, date.day).toLong()
        if (dayToLong < nowToLong) return false
    }
    return true
}
