package com.example.currencyconversions.ui.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.currencyconversions.databinding.FragmentDetailsBinding
import com.example.currencyconversions.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initViews()
        observeForStates()
        loadData()
        return binding.root
    }

    private fun initViews() {
        val args: DetailsFragmentArgs by navArgs()
        viewModel.fromCurrency = args.fromCurrency
        viewModel.toCurrency = args.toCurrency
    }

    private fun observeForStates() {
        observeLoadingState(viewModel.loading)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect {
                it?.let { showToast(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.historicalConversions.collect {
                if (it != null)
                    binding.rvHistorical.adapter = HistoricalAdapter(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topTenCurrencies.collect {
                if (it != null)
                    binding.rvOtherCurrencies.adapter = TopTenCurrenciesAdapter(it)
            }
        }
    }

    private fun loadData() {
        viewModel.fetchHistoricalData()
        viewModel.fetchTopTenCurrencies()
    }
}