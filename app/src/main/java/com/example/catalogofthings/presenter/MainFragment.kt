package com.example.catalogofthings.presenter

import com.example.catalogofthings.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.catalogofthings.databinding.FragmentMainBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class MainFragment: Fragment(R.layout.fragment_main) {
    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels {viewModelFactory}
}