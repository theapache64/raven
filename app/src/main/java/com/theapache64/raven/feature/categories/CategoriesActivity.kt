package com.theapache64.raven.feature.categories

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.theapache64.raven.R
import com.theapache64.raven.data.remote.Category
import com.theapache64.raven.databinding.ActivityCategoriesBinding
import com.theapache64.raven.feature.base.BaseActivity
import com.theapache64.raven.feature.quotes.QuotesActivity
import com.theapache64.raven.feature.wallpaper.SetWallpaperActivity
import com.theapache64.raven.utils.calladapter.flow.Resource
import com.theapache64.raven.utils.extensions.gone
import com.theapache64.raven.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesActivity :
    BaseActivity<ActivityCategoriesBinding, CategoriesViewModel>(R.layout.activity_categories) {
    override val viewModel: CategoriesViewModel by viewModels()


    override fun onCreate() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.categoriesResponse.observe(this, {
            when (it) {
                is Resource.Loading -> {
                    binding.rvCategories.gone()
                    binding.lvCategories.showLoading(R.string.categories_loading)
                }
                is Resource.Success -> {
                    binding.lvCategories.hideLoading()
                    binding.rvCategories.adapter = CategoriesAdapter(it.data,
                        object : CategoriesAdapter.Callback {
                            override fun onCategoryClicked(position: Int) {
                                // Launching categories
                                val category = it.data[position]
                                onCategoryClicked(category)
                            }
                        })
                    binding.rvCategories.visible()
                }
                is Resource.Error -> {
                    binding.lvCategories.showError(it.errorData)
                    binding.rvCategories.gone()
                }
            }

        })

        viewModel.shouldLaunchSetWallpaper.observe(this, Observer {
            if (it) {
                startActivity(SetWallpaperActivity.getStartIntent(this, null))
            }
        })
    }

    private fun onCategoryClicked(category: Category) {
        startActivity(
            QuotesActivity.getStartIntent(
                this@CategoriesActivity,
                category
            )
        )
    }

}