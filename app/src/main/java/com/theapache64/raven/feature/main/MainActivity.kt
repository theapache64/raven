package com.theapache64.raven.feature.main

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.theapache64.raven.R
import com.theapache64.raven.databinding.ActivityMainBinding
import com.theapache64.raven.feature.base.BaseActivity
import com.theapache64.raven.utils.DrawUtils
import com.theapache64.raven.utils.calladapter.flow.Resource
import com.theapache64.raven.utils.extensions.snackBar
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
        binding.rsFontSize.setValues(DrawUtils.DEFAULT_FONT_SIZE)
        binding.viewModel = viewModel

        viewModel.quote.observe(this, {
            when (it) {
                is Resource.Loading -> {
                    Timber.d("onCreate: Loading quote")
                    binding.lvMain.showLoading(R.string.any_loading)
                    binding.ivQuote.visibility = View.INVISIBLE
                    binding.bSetWallpaper.visibility = View.INVISIBLE
                }
                is Resource.Success -> {
                    Timber.i("onCreate: Quote loaded")
                    binding.lvMain.hideLoading()
                    binding.ivQuote.visibility = View.VISIBLE
                    binding.bSetWallpaper.visibility = View.VISIBLE

                    val bmp = DrawUtils.draw(
                        this,
                        binding.rsFontSize.values.first(),
                        it.data.quote,
                        getFont()
                    )

                    playExitAnimation {
                        binding.ivQuote.setImageBitmap(bmp)
                        viewModel.bmp = bmp

                        val enterAnim =
                            AnimationUtils.loadAnimation(this@MainActivity, R.anim.quote_enter)
                        binding.ivQuote.startAnimation(enterAnim)
                    }


                }
                is Resource.Error -> {
                    Timber.e("onCreate: Quote failed to load : ${it.errorData}")
                    binding.lvMain.showError(it.errorData)
                }
            }

        })

        viewModel.shouldShowInputDialog.observe(this, {
            MaterialDialog(this).show {
                title(R.string.dialog_title_text)
                input(
                    allowEmpty = false,
                    hintRes = R.string.hint_enter_text_here,
                ) { _: MaterialDialog, input: CharSequence ->
                    viewModel.onTextSubmit(input.toString())
                }
                positiveButton(R.string.action_done)
            }
        })

        viewModel.shouldUpdateText.observe(this, {
            updateQuote()
        })

        binding.rsFontSize.addOnChangeListener { slider, value, fromUser ->
            updateQuote()
        }

        viewModel.shouldChangeFont.observe(this, {
            updateQuote()
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
                    binding.bSetWallpaper.snackBar(R.string.main_wallpaper_set)
                    binding.lvMain.hideLoading()
                }
            }
        })
    }

    private fun updateQuote() {

        if (viewModel.currentQuote != null) {
            val font = getFont()

            val bmp = DrawUtils.draw(
                this,
                binding.rsFontSize.values.first(),
                viewModel.currentQuote!!.quote,
                font
            )

            binding.ivQuote.setImageBitmap(bmp)
            viewModel.bmp = bmp
        } else {
            toast(R.string.error_no_quote_selected)
        }
    }

    private fun getFont(): String {
        return if (viewModel.shouldChangeFont.value != null) {
            viewModel.shouldChangeFont.value!!
        } else {
            DrawUtils.RANDOM_FONTS.list.first()
        }
    }

    private fun playExitAnimation(onExited: () -> Unit) {
        val exitAnim = AnimationUtils.loadAnimation(this, R.anim.quote_exit)
        exitAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                onExited()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        binding.ivQuote.startAnimation(exitAnim)
    }
}