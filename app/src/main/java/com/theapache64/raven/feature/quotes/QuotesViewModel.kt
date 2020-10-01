package com.theapache64.raven.feature.quotes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.theapache64.raven.data.remote.Category
import com.theapache64.raven.data.repos.QuotesRepo
import com.theapache64.raven.feature.base.BaseViewModel

/**
 * Created by theapache64 : Oct 01 Thu,2020 @ 00:06
 */
class QuotesViewModel @ViewModelInject constructor(
    private val quotesRepo: QuotesRepo
) : BaseViewModel() {
    private val quotesRequest = MutableLiveData<Category>()
    val quotesResponse = quotesRequest.switchMap {
        quotesRepo.getQuotes(it).asLiveData(viewModelScope.coroutineContext)
    }

    private lateinit var category: Category

    fun init(category: Category) {
        this.category = category
        loadQuotes()
    }

    fun loadQuotes() {
        quotesRequest.value = category
    }

}