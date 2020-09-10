package com.theapache64.raven.data.remote

import com.theapache64.raven.utils.Config
import com.theapache64.raven.utils.calladapter.flow.Resource
import com.theapache64.retrosheet.core.Read
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 22:02
 */
interface RavenApi {

    @Read("SELECT * WHERE quote_date = :currentDate")
    @GET(Config.SHEET_NAME_QUOTES)
    fun getQuote(@Query("currentDate") currentDate: String): Flow<Resource<Quote>>
}