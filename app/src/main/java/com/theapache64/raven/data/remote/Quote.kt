package com.theapache64.raven.data.remote

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.theapache64.raven.utils.DateUtils
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize


/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 22:09
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class Quote(
    @Json(name = "readable_date")
    val readableDate: String,
    @Json(name = "quote_id")
    val quoteId: String,
    @Json(name = "category")
    val category: String,
    @Json(name = "quote")
    val quote: String
) : Parcelable {
    @IgnoredOnParcel
    val date: String = DateUtils.format(readableDate)
}