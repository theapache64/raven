package com.theapache64.raven.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.theapache64.raven.utils.DateUtils


/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 22:09
 */
@JsonClass(generateAdapter = true)
data class Quote(
    @Json(name = "readable_date")
    val readableDate: String,
    @Json(name = "quote_id")
    val quoteId: String,
    @Json(name = "category")
    val category: String,
    @Json(name = "quote")
    val quote: String
) {
    val date: String = DateUtils.format(readableDate)
}