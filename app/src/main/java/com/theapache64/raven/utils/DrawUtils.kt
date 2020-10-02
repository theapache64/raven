package com.theapache64.raven.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint


/**
 * Created by theapache64 : Sep 10 Thu,2020 @ 09:35
 */
object DrawUtils {

    const val DEFAULT_FONT_SIZE = 30f
    private const val WATERMARK_FONT = "QuiteMagicalRegular-8VA2.ttf"
    private const val FONT_VEGAN_STYLE = "VeganStylePersonalUse-5Y58.ttf"
    private const val SIGNATURE = "- raven -"

    val RANDOM_FONTS = arrayOf(
        "GoogleSans-Regular.ttf",
        "LemonJellyPersonalUse-dEqR.ttf",
        WATERMARK_FONT,
        "BeautifulPeoplePersonalUse-PYP2.ttf",
        "Countryside-YdKj.ttf",
        FONT_VEGAN_STYLE,
        "QuickKissPersonalUse-PxlZ.ttf",
        "BeautifulPeoplePersonalUse-dE0g.ttf",
        "CountrysideTwo-r9WO.ttf",
        "BeautyMountainsPersonalUse-od7z.ttf"
    ).toList().toCycleList()

    val CUSTOM_LINE_HEIGHTS = mapOf(
        FONT_VEGAN_STYLE to 2f
    )

    fun draw(context: Context, fontSize: Float, text: String, font: String): Bitmap {
        val dm = Resources.getSystem().displayMetrics
        val width = dm.widthPixels
        val height = dm.heightPixels
        val bmp = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bmp)

        // First black background
        val black = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
        }

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), black)

        // new anti-aliased Paint
        val mainTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = (fontSize * dm.density)
            typeface = Typeface.createFromAsset(context.assets, "fonts/$font")
        }


        // set text width to canvas width minus 16dp padding
        val textWidth = canvas.width - (100 * dm.density)

        val lineHeight = CUSTOM_LINE_HEIGHTS[font] ?: 1f

        // init StaticLayout for text
        val mainText = StaticLayout(
            text,
            mainTextPaint,
            textWidth.toInt(),
            Layout.Alignment.ALIGN_CENTER,
            lineHeight,
            0.0f,
            true
        )


        val watermarkPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.GRAY
            textSize = ((fontSize * 0.75f) * dm.density)
            typeface = Typeface.createFromAsset(context.assets, "fonts/$WATERMARK_FONT")
        }

        val watermarkText = StaticLayout(
            SIGNATURE,
            watermarkPaint,
            textWidth.toInt(),
            Layout.Alignment.ALIGN_CENTER,
            1.0f,
            0.0f,
            true
        )

        val textHeight = mainText.height
        val cx = (width - textWidth) / 2
        val cy = (height - textHeight) / 2

        canvas.save()
        canvas.translate(cx, cy.toFloat())
        mainText.draw(canvas)
        canvas.restore()

        canvas.save()
        canvas.translate(cx, height.toFloat() - 100)
        watermarkText.draw(canvas)
        canvas.restore()


        return bmp
    }
}