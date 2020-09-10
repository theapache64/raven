package com.theapache64.raven.worker

import android.app.WallpaperManager
import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.theapache64.raven.data.repos.QuotesRepo
import com.theapache64.raven.utils.DrawUtils
import com.theapache64.raven.utils.QuoteUtils
import com.theapache64.raven.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.collect
import timber.log.Timber

/**
 * Created by theapache64 : Sep 10 Thu,2020 @ 08:46
 */
class SetWallpaperWorker @WorkerInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val quotesRepo: QuotesRepo
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_TYPE = "type"
    }

    override suspend fun doWork(): Result {
        var result: Result = Result.success()

        when (val type = inputData.getString(KEY_TYPE)) {
            QuoteUtils.TYPE_TODAY -> {
                Timber.d("doWork: Type is today")


                quotesRepo.getQuote(QuoteUtils.getQuoteIdToday()).collect {
                    when (it) {
                        is Resource.Loading -> {
                            Timber.d("doWork: Quote is loading")
                        }
                        is Resource.Success -> {
                            Timber.d("doWork: Quote has loaded: $it")
                            val bitmap = DrawUtils.draw(context, it.data.quote)
                            WallpaperManager.getInstance(context)
                                .setBitmap(bitmap)
                        }
                        is Resource.Error -> {
                            Timber.d("doWork: Quote failed to load")
                            result = Result.retry()
                        }
                    }
                }
            }

            QuoteUtils.TYPE_RANDOM -> {
                Timber.d("doWork: Type is random")
            }

            else -> throw IllegalArgumentException("Undefined type '$type'")
        }

        return result
    }
}