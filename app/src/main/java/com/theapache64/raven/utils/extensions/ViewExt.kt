package com.theapache64.raven.utils.extensions

import android.view.View

/**
 * Created by theapache64 : Sep 30 Wed,2020 @ 23:33
 */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}