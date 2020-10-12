package com.theapache64.raven.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Created by theapache64 : Oct 12 Mon,2020 @ 22:33
 */
@Module
@InstallIn(ApplicationComponent::class)
object PrefModule {

    @Singleton
    @Provides
    fun providePref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("raven_pref", Context.MODE_PRIVATE)
    }
}