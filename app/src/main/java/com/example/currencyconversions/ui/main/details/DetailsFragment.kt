package com.example.currencyconversions.ui.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.currencyconversions.R
import com.example.currencyconversions.databinding.FragmentDetailsBinding
import com.example.currencyconversions.ui.base.BaseFragment
import com.example.currencyconversions.utils.Utils
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

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

        binding.chart.setPinchZoom(true)
        binding.chart.legend.form = Legend.LegendForm.CIRCLE
        binding.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.extraBottomOffset = 15F
        binding.chart.axisRight.isEnabled = false
        binding.chart.description.isEnabled = false
        binding.chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index =
                    min(value.roundToInt(), viewModel.historicalConversions.value?.size ?: 1) - 1
                val date =
                    Utils.dateFormat(viewModel.historicalConversions.value!![max(index, 0)].date)
                return SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
            }
        }
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
                if (it != null) {
                    binding.rvHistorical.adapter = HistoricalAdapter(it)
                    fillGraph()
                }
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

    private fun fillGraph() {

        val ratiosValues = ArrayList<Entry>()
        for (count in 0 until viewModel.historicalConversions.value!!.size) {
            ratiosValues.add(
                Entry(
                    (count + 1).toFloat(),
                    viewModel.historicalConversions.value!![count].ratio.toFloat()
                )
            )
        }

        val ratiosLineDataSet = LineDataSet(
            ratiosValues,
            String.format("%s Ratios to %s", viewModel.toCurrency, viewModel.fromCurrency)
        )
        ratiosLineDataSet.color = resources.getColor(R.color.purple_700)
        ratiosLineDataSet.setDrawFilled(true)
        ratiosLineDataSet.fillDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.graph_color)
        ratiosLineDataSet.setCircleColor(resources.getColor(R.color.purple_700))
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(ratiosLineDataSet)
        val data = LineData(dataSets)
        binding.chart.xAxis.labelCount = max(2, viewModel.historicalConversions.value!!.size - 1)
        binding.chart.data = data
        binding.chart.animateY(1000)
    }
}