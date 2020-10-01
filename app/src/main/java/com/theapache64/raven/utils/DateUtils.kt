package com.theapache64.raven.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by theapache64 : Oct 01 Thu,2020 @ 20:53
 */
object DateUtils {
    private val inputDateFormat by lazy { SimpleDateFormat("dd-MM-yyyy", Locale.US) }
    private val outputDateFormat by lazy { SimpleDateFormat("dd MMMM", Locale.US) }
    fun format(input: String) = outputDateFormat.format(inputDateFormat.parse(input)!!)
    fun getToday(): String {
        return inputDateFormat.format(Date())
    }
}