package net.terminal_end.tasrankingviewer.util

import com.github.kittinunf.result.Result
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by S-Shimotori on 6/1/16.
 */

fun String.toCalendar(): Result<Calendar, ParseException> {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return try {
        val calendar = Calendar.getInstance()
        calendar.time = format.parse(this)
        Result.Success(calendar)
    } catch(e: ParseException) {
        Result.Failure(e)
    }
}