package com.theapache64.raven

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import timber.log.Timber

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 21:53
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        ViewPump.builder()
            .addInterceptor(
                CalligraphyInterceptor(
                    CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/GoogleSans-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
                )
            ).build().let { viewPump ->
                ViewPump.init(viewPump)
            }
    }
}