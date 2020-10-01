package com.theapache64.raven.data.repos

import com.theapache64.raven.data.remote.Category
import com.theapache64.raven.data.remote.RavenApi
import javax.inject.Inject

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 22:11
 */
class QuotesRepo @Inject constructor(
    private val ravenApi: RavenApi
) {
    fun getQuote(quoteId: String) = ravenApi.getQuote(quoteId)
    fun getAllQuotes() = ravenApi.getAllQuotes()
    fun getQuotes(category: Category) = ravenApi.getQuotes(category.title)
}