package com.example.currencyconversions.ui.main.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconversions.R
import com.example.currencyconversions.databinding.ItemListCurrencyBinding
import com.example.currencyconversions.domain.entity.CurrencyRatio

class TopTenCurrenciesAdapter(private val currencyRatioList: List<CurrencyRatio>) :
    RecyclerView.Adapter<TopTenCurrenciesAdapter.CurrencyHolder>() {
    private var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding: ItemListCurrencyBinding =
            DataBindingUtil.inflate(inflater!!, R.layout.item_list_currency, parent, false)
        return CurrencyHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        holder.itemBinding.currencyRatio = currencyRatioList[position]
    }

    override fun getItemCount(): Int {
        return currencyRatioList.size
    }

    inner class CurrencyHolder(val itemBinding: ItemListCurrencyBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}