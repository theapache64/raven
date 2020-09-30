package com.theapache64.raven.di.modules

import com.squareup.moshi.Moshi
import com.theapache64.raven.data.remote.RavenApi
import com.theapache64.raven.utils.calladapter.flow.FlowResourceCallAdapterFactory
import com.theapache64.retrosheet.RetrosheetInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 21:17
 * Copyright (c) 2020
 * All rights reserved
 */
@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }


    @Singleton
    @Provides
    fun provideRetrosheetInterceptor(): RetrosheetInterceptor {
        return RetrosheetInterceptor.Builder()
            .setLogging(true)
            .addSheet(
                "quotes",
                "readable_date", "quote_id", "category", "quote"
            )
            .addSheet(
                "categories",
                "id", "title", "total_quotes"
            )
            .build()
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(retrosheetInterceptor: RetrosheetInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retrosheetInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRavenApi(okHttpClient: OkHttpClient, moshi: Moshi): RavenApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://docs.google.com/spreadsheets/d/1eDOjClNJGgrROftn9zW69WKNOnQVor_zrF8yo0v5KGs/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
            .create(RavenApi::class.java)
    }

}