package com.currency.converter.remote

import com.currency.converter.retrofit.CurrencyConverterService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val currencyConverterService: CurrencyConverterService) {

    suspend fun getExchangeRates() = currencyConverterService.getExchangeRates()
}