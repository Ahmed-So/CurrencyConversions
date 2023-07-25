package com.example.currencyconversions.ui.main.convertCurrency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.currencyconversions.databinding.FragmentConvertCurrencyBinding
import com.example.currencyconversions.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConvertCurrencyFragment : BaseFragment(), ConvertCurrencyHandlers {

    private lateinit var binding: FragmentConvertCurrencyBinding
    private val viewModel: ConvertCurrencyViewModel by viewModels()
    private var swapping: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConvertCurrencyBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.handlers = this
        binding.viewModel = viewModel

        initViews()
        initEvents()
        observeForStates()
        loadData()
        return binding.root
    }

    private fun initViews() {
    }

    private fun initEvents() {
        binding.spCurrencyFrom.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    if (swapping || viewModel.loading.value)
                        return
                    viewModel.convert(
                        true,
                        binding.spCurrencyFrom.selectedItem as String,
                        binding.spCurrencyTo.selectedItem as String
                    )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        binding.spCurrencyTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (swapping || viewModel.loading.value)
                    return
                viewModel.convert(
                    false,
                    binding.spCurrencyTo.selectedItem as String,
                    binding.spCurrencyFrom.selectedItem as String
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        binding.etAmountFrom.doAfterTextChanged {
            if (!binding.etAmountFrom.isFocused || swapping || it.toString().isBlank())
                return@doAfterTextChanged
            viewModel.convert(
                true,
                binding.spCurrencyFrom.selectedItem as String,
                binding.spCurrencyTo.selectedItem as String
            )
        }
        binding.etAmountTo.doAfterTextChanged {
            if (!binding.etAmountTo.isFocused || swapping || it.toString().isBlank())
                return@doAfterTextChanged
            viewModel.convert(
                false,
                binding.spCurrencyTo.selectedItem as String,
                binding.spCurrencyFrom.selectedItem as String
            )
        }
    }

    private fun observeForStates() {
        observeLoadingState(viewModel.loading)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect {
                it?.let { showToast(it) }
            }
        }
    }

    private fun loadData() {
        viewModel.fetchAllCurrencies()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allCurrencies.collect {
                if (it == null)
                    return@collect

                val adapter = ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    viewModel.allCurrencies.value!!
                )
                binding.spCurrencyFrom.adapter = adapter
                binding.spCurrencyTo.adapter = adapter
            }
        }
    }

    override fun swapCurrencies() {
        if (binding.spCurrencyFrom.selectedItemPosition == binding.spCurrencyTo.selectedItemPosition)
            return

        swapping = true
        val fromSelection = binding.spCurrencyFrom.selectedItemPosition
        binding.spCurrencyFrom.setSelection(binding.spCurrencyTo.selectedItemPosition)
        binding.spCurrencyTo.setSelection(fromSelection)
        val fromAmount = binding.etAmountFrom.text.toString()
        binding.etAmountFrom.setText(binding.etAmountTo.text.toString())
        binding.etAmountTo.setText(fromAmount)
        swapping = false
    }

    override fun showDetails() {
    }
}