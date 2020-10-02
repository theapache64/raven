package com.theapache64.raven.feature.quotes

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.theapache64.raven.R
import com.theapache64.raven.data.remote.Category
import com.theapache64.raven.databinding.ActivityQuotesBinding
import com.theapache64.raven.feature.base.BaseActivity
import com.theapache64.raven.feature.wallpaper.SetWallpaperActivity
import com.theapache64.raven.utils.calladapter.flow.Resource
import com.theapache64.raven.utils.extensions.invisible
import com.theapache64.raven.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuotesActivity :
    BaseActivity<ActivityQuotesBinding, QuotesViewModel>(R.layout.activity_quotes) {

    companion object {
        private const val KEY_CATEGORY = "category"

        fun getStartIntent(context: Context, category: Category): Intent {
            return Intent(context, QuotesActivity::class.java).apply {
                // data goes here
                putExtra(KEY_CATEGORY, category)
            }
        }
    }

    override val viewModel: QuotesViewModel by viewModels()

    override fun onCreate() {
        binding.viewModel = viewModel
        val category = getParcelableOrThrow<Category>(KEY_CATEGORY)
        viewModel.init(category)

        viewModel.quotesResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.lvQuotes.showLoading(
                        getString(R.string.quotes_loading, category.title)
                    )
                    binding.rvQuotes.invisible()
                }
                is Resource.Success -> {
                    binding.lvQuotes.hideLoading()
                    val quotes = it.data.reversed()

                    // Setting adapter
                    binding.rvQuotes.adapter = QuotesAdapter(
                        this,
                        quotes
                    ) { position ->
                        val quote = quotes[position]
                        startActivity(SetWallpaperActivity.getStartIntent(this, quote))
                    }
                    binding.rvQuotes.visible()
                }
                is Resource.Error -> {
                    binding.lvQuotes.showError(it.errorData)
                }
            }
        })
    }

}