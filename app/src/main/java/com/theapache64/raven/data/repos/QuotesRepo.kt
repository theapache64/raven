package com.theapache64.raven.data.repos

import com.theapache64.raven.data.remote.RavenApi
import javax.inject.Inject

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 22:11
 */
class QuotesRepo @Inject constructor(
    private val ravenApi: RavenApi
) {
    fun getQuote(date: String) = ravenApi.getQuote(date)
}