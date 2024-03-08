package com.currency.converter.retrofit

import com.currency.converter.model.ExchangeRate
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyConverterService {

    @GET("EUR")
    suspend fun getExchangeRates() : Response<ExchangeRate>
}