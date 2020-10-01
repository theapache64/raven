package com.theapache64.raven.data.remote

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


/**
 * Created by theapache64 : Sep 30 Wed,2020 @ 21:41
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class Category(
    @Json(name = "id")
    val id: Int, // 1
    @Json(name = "title")
    val title: String,
    @Json(name = "total_quotes")
    val totalQuotes: Int // 0
) : Parcelable