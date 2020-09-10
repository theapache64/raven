package com.theapache64.raven.feature.main

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.core.view.postDelayed
import com.theapache64.raven.R
import com.theapache64.raven.databinding.ActivityMainBinding
import com.theapache64.raven.feature.base.BaseActivity
import com.theapache64.raven.utils.DrawUtils
import com.theapache64.raven.utils.calladapter.flow.Resource
import com.theapache64.raven.utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

                    binding.ivQuote.postDelayed(2000) {
                        val anim = AnimationUtils.loadAnimation(this, R.anim.fold_down)
                        binding.ivQuote.startAnimation(anim)
                    }
                }
                is Resource.Error -> {
                    Timber.e("onCreate: Quote failed to load : ${it.errorData}")
                    binding.lvMain.showError(it.errorData)
                }
            }

        })

        binding.csrlMain.setOnRefreshListener {
            viewModel.onSwipedToRefresh()
        }

        viewModel.shouldSetWallpaper.observe(this, {
            binding.lvMain.showLoading(R.string.main_setting_wallpaper)
            GlobalScope.launch {
                WallpaperManager.getInstance(this@MainActivity)
                    .setBitmap(viewModel.bmp)

                runOnUiThread {
                    toast(R.string.main_wallpaper_set)
                    binding.lvMain.hideLoading()
                }
            }
        })
    }
}