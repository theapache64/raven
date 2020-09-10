package com.theapache64.raven.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by theapache64 : Sep 10 Thu,2020 @ 10:50
 */
object QuoteUtils {
    const val TYPE_TODAY = "today"
    const val TYPE_RANDOM = "random"

    fun getQuoteIdToday(): String {
        val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}