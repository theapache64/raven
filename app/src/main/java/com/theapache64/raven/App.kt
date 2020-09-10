package com.theapache64.raven

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.theapache64.raven.utils.QuoteUtils
import com.theapache64.raven.worker.SetWallpaperWorker
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 21:53
 */
@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

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

        val work = PeriodicWorkRequestBuilder<SetWallpaperWorker>(24L, TimeUnit.HOURS)
            .setInputData(
                workDataOf(
                    SetWallpaperWorker.KEY_TYPE to QuoteUtils.TYPE_TODAY
                )
            )
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                SetWallpaperWorker::class.java.name,
                ExistingPeriodicWorkPolicy.REPLACE,
                work
            )

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}