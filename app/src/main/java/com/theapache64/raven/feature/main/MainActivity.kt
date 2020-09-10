package com.theapache64.raven.feature.main

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.theapache64.raven.R
import com.theapache64.raven.databinding.ActivityMainBinding
import com.theapache64.raven.feature.base.BaseActivity
import com.theapache64.raven.utils.DrawUtils
import com.theapache64.raven.utils.calladapter.flow.Resource
import com.theapache64.raven.utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override val viewModel: MainViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        binding.viewModel = viewModel

        viewModel.quote.observe(this, {
            when (it) {
                is Resource.Loading -> {
                    Timber.d("onCreate: Loading quote")
                    binding.lvMain.showLoading(R.string.any_loading)
                    binding.ivQuote.visibility = View.INVISIBLE
                }
                is Resource.Success -> {
                    Timber.i("onCreate: Quote loaded")
                    binding.lvMain.hideLoading()
                    binding.ivQuote.visibility = View.VISIBLE

                    val bmp = DrawUtils.draw(
                        this,
                        it.data.quote
                    )
                    binding.ivQuote.setImageBitmap(bmp)
                    viewModel.bmp = bmp
                }
                is Resource.Error -> {
                    Timber.e("onCreate: Quote failed to load : ${it.errorData}")
                    binding.lvMain.showError(it.errorData)
                }
            }

        })

        viewModel.shouldSetWallpaper.observe(this, {
            WallpaperManager.getInstance(this)
                .setBitmap(viewModel.bmp)

            toast(R.string.main_wallpaper_set)
        })
    }
}