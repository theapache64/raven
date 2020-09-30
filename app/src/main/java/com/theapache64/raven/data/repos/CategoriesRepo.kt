package com.theapache64.raven.data.repos

import com.theapache64.raven.data.remote.RavenApi
import javax.inject.Inject

/**
 * Created by theapache64 : Sep 30 Wed,2020 @ 23:26
 */
class CategoriesRepo @Inject constructor(
    private val ravenApi: RavenApi
) {

    fun getCategories() = ravenApi.getCategories()
}