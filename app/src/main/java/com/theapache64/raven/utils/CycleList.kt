package com.theapache64.raven.utils

import java.io.IOException

/**
 * Created by theapache64 : Sep 12 Sat,2020 @ 12:02
 */
class CycleList<T>(val list: List<T>) {

    private var currentIndex = -1

    fun next(): T {
        if (list.isEmpty()) {
            throw IOException("Can't call next. List is empty")
        }

        if (currentIndex == -1) {
            // return first item
            currentIndex = 0
            return list.first()
        }

        var nextIndex = ++currentIndex
        if (nextIndex == list.size) {
            // reached end
            nextIndex = 0
            currentIndex = nextIndex
        }


        return list[nextIndex]
    }

    fun prev(): T {
        if (list.isEmpty()) {
            throw IOException("Can't call next. List is empty")
        }

        if (currentIndex == -1) {
            currentIndex = list.size - 1
            return list.last()
        }

        var nextIndex = --currentIndex

        if (nextIndex == -1) {
            nextIndex = list.size - 1
            currentIndex = nextIndex
        }

        return list[nextIndex]
    }

    fun first(): T {
        currentIndex = 0
        return list.first()
    }


}

fun <T> List<T>.toCycleList(): CycleList<T> {
    return CycleList(this)
}