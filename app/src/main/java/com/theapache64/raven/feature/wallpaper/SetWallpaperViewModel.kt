package com.theapache64.raven.feature.wallpaper

import android.graphics.Bitmap
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.theapache64.raven.R
import com.theapache64.raven.data.remote.Quote
import com.theapache64.raven.data.repos.QuotesRepo
import com.theapache64.raven.feature.base.BaseViewModel
import com.theapache64.raven.utils.DateUtils
import com.theapache64.raven.utils.DrawUtils
import com.theapache64.raven.utils.QuoteUtils
import com.theapache64.raven.utils.calladapter.flow.Resource
import com.theapache64.raven.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 21:57
 */
class SetWallpaperViewModel @ViewModelInject constructor(
    private val quotesRepo: QuotesRepo
) : BaseViewModel() {

    private var givenQuote: Quote? = null
    var currentQuote: Quote? = null

    var bmp: Bitmap? = null

    private val _shouldSetWallpaper = SingleLiveEvent<Boolean>()
    val shouldSetWallpaper: LiveData<Boolean> = _shouldSetWallpaper

    private val shownQuotes = mutableSetOf<String>()
    private val _shouldGenerateQuote = MutableLiveData<String>()
    private var allQuotes: List<Quote>? = null

    private val _shouldChangeFont = MutableLiveData<String>()
    val shouldChangeFont: LiveData<String> = _shouldChangeFont

    private val _shouldShowInputDialog = SingleLiveEvent<Boolean>()
    val shouldShowInputDialog: LiveData<Boolean> = _shouldShowInputDialog

    private val _shouldUpdateText = MutableLiveData<Boolean>()
    val shouldUpdateText: LiveData<Boolean> = _shouldUpdateText

    val quote: LiveData<Resource<Quote>> = _shouldGenerateQuote.switchMap { type ->
        when (type) {
            QuoteUtils.TYPE_TODAY -> {
                // get today quote
                val todayQuoteId = QuoteUtils.getQuoteIdToday()
                quotesRepo.getQuote(todayQuoteId)
                    .onEach {
                        if (it is Resource.Success) {
                            currentQuote = it.data
                        }
                    }
                    .asLiveData(viewModelScope.coroutineContext)

            }

            QuoteUtils.TYPE_GIVEN -> {
                currentQuote = givenQuote
                MutableLiveData(Resource.Success(null, givenQuote!!))
            }
            else -> {
                // get random quote
                liveData<Resource<Quote>> {

                    if (allQuotes == null) {
                        Timber.d("Getting quote from network: ")
                        quotesRepo.getAllQuotes()
                            .collect { it ->
                                when (it) {
                                    is Resource.Loading -> {
                                        // do nothing
                                        emit(Resource.Loading())
                                    }
                                    is Resource.Success -> {
                                        allQuotes = it.data.filter { it.quote.isNotBlank() }
                                        currentQuote = allQuotes!!.random()
                                        shownQuotes.clear()
                                        shownQuotes.add(currentQuote!!.quoteId)
                                        emit(Resource.Success(null, currentQuote!!))
                                    }
                                    is Resource.Error -> {
                                        emit(Resource.Error(it.errorData))
                                    }
                                }
                            }
                    } else {
                        Timber.d("Getting quote from cache: ")
                        emit(Resource.Loading())
                        currentQuote = getRandomQuote()
                        shownQuotes.add(currentQuote!!.quoteId)
                        emit(Resource.Success(null, currentQuote!!))
                    }

                }
            }
        }
    }

    private fun getRandomQuote(): Quote {
        val quote = allQuotes!!.random()

        if (shownQuotes.size == allQuotes!!.size) {
            // full
            shownQuotes.clear()
        }
        return if (shownQuotes.contains(quote.quoteId)) {
            getRandomQuote()
        } else {
            quote
        }
    }

    fun init(quote: Quote?) {
        if (quote == null) {
            // Load today's quote
            _shouldGenerateQuote.value = QuoteUtils.TYPE_TODAY
        } else {
            // Load given quote
            givenQuote = quote
            _shouldGenerateQuote.value = QuoteUtils.TYPE_GIVEN
        }
    }


    fun onSwipedToRefresh() {
        _shouldGenerateQuote.value = QuoteUtils.TYPE_RANDOM
    }

    fun onSetWallpaperClicked() {
        if (bmp != null) {
            _shouldSetWallpaper.value = true
        } else {
            _toastMsg.value = R.string.error_no_quote_selected
        }
    }

    fun onSwipeTop() {
        _shouldShowInputDialog.value = true
    }

    fun onSwipeLeft() {
        _shouldChangeFont.value = DrawUtils.RANDOM_FONTS.prev()
    }

    fun onSwipeRight() {
        _shouldChangeFont.value = DrawUtils.RANDOM_FONTS.next()
    }


    fun onTextToolClicked() {
        _shouldChangeFont.value = DrawUtils.RANDOM_FONTS.first()
        _toastMsg.value = R.string.main_toast_default_font_set
    }

    fun onTextSubmit(input: String) {
        currentQuote = Quote(DateUtils.getToday(), "", "", input)
        _shouldUpdateText.value = true
    }

    private val _shouldShare = SingleLiveEvent<Boolean>()
    val shouldShare: LiveData<Boolean> = _shouldShare
    fun onShareClicked() {
        _shouldShare.value = true
    }

    fun onFailedToShare() {
        _toastMsg.value = R.string.wallpaper_error_share_failed
    }


}