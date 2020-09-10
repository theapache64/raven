package com.theapache64.raven.feature.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.theapache64.raven.data.remote.Quote
import com.theapache64.raven.data.repos.QuotesRepo
import com.theapache64.raven.feature.base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 21:57
 */
class MainViewModel @ViewModelInject constructor(
    private val quotesRepo: QuotesRepo
) : BaseViewModel() {


    init {

    }
}