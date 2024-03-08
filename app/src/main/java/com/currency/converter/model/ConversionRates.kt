package com.currency.converter.model

import androidx.annotation.Keep

@Keep
data class ConversionRates(
    val id: Int,
    val currencyCode: String,
    val currencyRate: Double,
    val conversionRate: Double
)
