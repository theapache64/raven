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

    fun draw(context: Context, text: String): Bitmap {
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
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = (30 * dm.density)
            typeface = Typeface.createFromAsset(context.assets, "fonts/GoogleSans-Regular.ttf")
        }

        // set text width to canvas width minus 16dp padding
        val textWidth = canvas.width - (100 * dm.density)

        // init StaticLayout for text
        val textLayout = StaticLayout(
            text,
            textPaint,
            textWidth.toInt(),
            Layout.Alignment.ALIGN_CENTER,
            1.0f,
            0.0f,
            true
        )

        val textHeight = textLayout.height
        val x = (width - textWidth) / 2
        val y = (height - textHeight) / 2

        canvas.save()
        canvas.translate(x, y.toFloat())
        textLayout.draw(canvas)
        canvas.restore()

        return bmp
    }
}