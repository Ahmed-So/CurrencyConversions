package com.example.currencyconversions.ui.main.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconversions.R
import com.example.currencyconversions.databinding.ItemListHistoryBinding
import com.example.currencyconversions.domain.entity.CurrencyRatio

class HistoricalAdapter(private val currencyRatioList: List<CurrencyRatio>) :
    RecyclerView.Adapter<HistoricalAdapter.HistoricalHolder>() {
    private var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoricalHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding: ItemListHistoryBinding =
            DataBindingUtil.inflate(inflater!!, R.layout.item_list_history, parent, false)
        return HistoricalHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoricalHolder, position: Int) {
        holder.itemBinding.currencyRatio = currencyRatioList[position]
    }

    override fun getItemCount(): Int {
        return currencyRatioList.size
    }

    inner class HistoricalHolder(val itemBinding: ItemListHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}