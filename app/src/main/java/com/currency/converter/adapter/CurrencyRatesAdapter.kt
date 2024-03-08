package com.currency.converter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.currency.converter.R
import com.currency.converter.databinding.CurrencyRateLayoutBinding
import com.currency.converter.model.ConversionRates
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRatesAdapter @Inject constructor(diffUtilCallback : DiffUtil.ItemCallback<ConversionRates>)
    : ListAdapter<ConversionRates, CurrencyRatesAdapter.ViewHolder>(diffUtilCallback) {

    inner class ViewHolder(private val binding: CurrencyRateLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(conversionRate : ConversionRates) {
            binding.currencyCode.text = conversionRate.currencyCode
            binding.currencyConversionRate.text = String.format("%.2f", conversionRate.conversionRate)
            val oneEur = binding.currencyRateLayout.context.getString(R.string.one_eur_to_pkr)
            val oneEurTxt = "${String.format(Locale.US, oneEur, conversionRate.currencyRate)} ${conversionRate.currencyCode}"
            binding.oneEur.text = oneEurTxt
            val oneCur = binding.currencyRateLayout.context.getString(R.string.one_cur_to_eur)
            val oneCurRate = 1 / conversionRate.currencyRate
            val oneCurTxt = "1 ${conversionRate.currencyCode} = ${String.format(Locale.US, oneCur, oneCurRate)}"
            binding.oneCur.text = oneCurTxt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CurrencyRateLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversionRate = getItem(holder.adapterPosition)
        holder.bind(conversionRate)
    }
}

@Singleton
class CurrenciesDiff : DiffUtil.ItemCallback<ConversionRates>() {
    override fun areItemsTheSame(oldItem: ConversionRates, newItem: ConversionRates): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ConversionRates, newItem: ConversionRates): Boolean {
        return oldItem == newItem
    }
}