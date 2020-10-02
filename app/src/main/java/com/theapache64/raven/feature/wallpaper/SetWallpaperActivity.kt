package com.theapache64.raven.feature.wallpaper

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.theapache64.raven.R
import com.theapache64.raven.data.remote.Quote
import com.theapache64.raven.databinding.ActivitySetWallpaperBinding
import com.theapache64.raven.feature.base.BaseActivity
import com.theapache64.raven.utils.DrawUtils
import com.theapache64.raven.utils.OnSwipeTouchListener
import com.theapache64.raven.utils.calladapter.flow.Resource
import com.theapache64.raven.utils.extensions.invisible
import com.theapache64.raven.utils.extensions.snackBar
import com.theapache64.raven.utils.extensions.toast
import com.theapache64.raven.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@AndroidEntryPoint
class SetWallpaperActivity :
    BaseActivity<ActivitySetWallpaperBinding, SetWallpaperViewModel>(R.layout.activity_set_wallpaper) {

    companion object {
        private const val SHARE_TEXT =
            "by @RobinSharma. Shared via @Raven\n\nCheckout : https://github.com/theapache64/raven"
        private const val KEY_QUOTE = "quote"
        fun getStartIntent(context: Context, quote: Quote?): Intent {
            return Intent(context, SetWallpaperActivity::class.java).apply {
                // data goes here
                putExtra(KEY_QUOTE, quote)
            }
        }
    }

    override val viewModel: SetWallpaperViewModel by viewModels()

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate() {
        binding.rsFontSize.setValues(DrawUtils.DEFAULT_FONT_SIZE)
        binding.viewModel = viewModel

        val quote = getParcelable<Quote>(KEY_QUOTE)
        viewModel.init(quote)

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
                            AnimationUtils.loadAnimation(
                                this@SetWallpaperActivity,
                                R.anim.quote_enter
                            )
                        binding.ivQuote.startAnimation(enterAnim)
                    }


                }
                is Resource.Error -> {
                    Timber.e("onCreate: Quote failed to load : ${it.errorData}")
                    binding.lvMain.showError(it.errorData)
                }
            }

        })


        binding.ivQuote.setOnTouchListener(object :
            OnSwipeTouchListener(this@SetWallpaperActivity) {
            override fun onSwipeRight() {
                Timber.d("onSwipeRight: Swiped right")
                viewModel.onSwipeRight()
            }

            override fun onSwipeTop() {
                viewModel.onSwipeTop()
            }

            override fun onSwipeLeft() {
                Timber.d("onSwipeLeft: Swiped left")
                viewModel.onSwipeLeft()
            }

            override fun onSwipeBottom() {
                Timber.d("onSwipeBottom: Swiped down")
                viewModel.onSwipedToRefresh()
            }
        })

        viewModel.shouldShowInputDialog.observe(this, {
            MaterialDialog(this).show {
                title(R.string.dialog_title_text)
                input(
                    allowEmpty = false,
                    prefill = viewModel.currentQuote?.quote,
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

        viewModel.shouldSetWallpaper.observe(this, {
            binding.lvMain.showLoading(R.string.main_setting_wallpaper)
            binding.ivQuote.invisible()
            GlobalScope.launch {
                WallpaperManager.getInstance(this@SetWallpaperActivity)
                    .setBitmap(viewModel.bmp)

                runOnUiThread {
                    binding.bSetWallpaper.snackBar(R.string.main_wallpaper_set)
                    binding.lvMain.hideLoading()
                    binding.ivQuote.visible()
                }
            }
        })

        viewModel.shouldShare.observe(this, Observer {
            if (it && viewModel.bmp != null) {
                val uri = saveImage(viewModel.bmp!!)
                if (uri != null) {
                    shareImageUri(uri)
                } else {
                    viewModel.onFailedToShare()
                }
            }
        })
    }

    private fun shareImageUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, SHARE_TEXT)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "image/png"
        }
        startActivity(intent)
    }

    private fun saveImage(image: Bitmap): Uri? {
        val imagesFolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(this, "com.theapache64.raven.fileprovider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            Timber.e("saveImage: IOException while trying to write file for sharing: ${e.message}")
        }
        return uri
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