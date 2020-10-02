package com.theapache64.raven.feature.categories

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.theapache64.raven.data.repos.CategoriesRepo
import com.theapache64.raven.feature.base.BaseViewModel
import com.theapache64.raven.utils.livedata.SingleLiveEvent

/**
 * Created by theapache64 : Sep 30 Wed,2020 @ 21:37
 */
class CategoriesViewModel @ViewModelInject constructor(
    private val categoriesRepo: CategoriesRepo
) : BaseViewModel() {

    private val categoriesRequest = MutableLiveData<Boolean>()
    val categoriesResponse = categoriesRequest.switchMap {
        categoriesRepo.getCategories().asLiveData(viewModelScope.coroutineContext)
    }

    init {
        loadCategories()
    }

    fun loadCategories() {
        categoriesRequest.value = true
    }

    private val _shouldLaunchSetWallpaper = SingleLiveEvent<Boolean>()
    val shouldLaunchSetWallpaper: LiveData<Boolean> = _shouldLaunchSetWallpaper

    fun onSetWallpaperClicked() {
        _shouldLaunchSetWallpaper.value = true
    }

}

