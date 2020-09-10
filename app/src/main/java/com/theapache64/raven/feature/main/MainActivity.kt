package com.theapache64.raven.feature.main

import android.annotation.SuppressLint
import androidx.activity.viewModels
import com.theapache64.raven.R
import com.theapache64.raven.databinding.ActivityMainBinding
import com.theapache64.raven.feature.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override val viewModel: MainViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate() {

    }
}