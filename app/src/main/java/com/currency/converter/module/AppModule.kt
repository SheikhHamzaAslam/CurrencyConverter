package com.currency.converter.module

import androidx.recyclerview.widget.DiffUtil
import com.currency.converter.adapter.CurrenciesDiff
import com.currency.converter.adapter.CurrencyRatesAdapter
import com.currency.converter.model.ConversionRates
import com.currency.converter.retrofit.CurrencyConverterService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://v6.exchangerate-api.com/v6/a6f97891732677a84ec18b3a/latest/"
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideCurrencyService(retrofit: Retrofit) : CurrencyConverterService =
        retrofit.create(CurrencyConverterService::class.java)

    @Singleton
    @Provides
    fun provideDiffUtil() : DiffUtil.ItemCallback<ConversionRates> =
        CurrenciesDiff()

    @Singleton
    @Provides
    fun provideCurrencyRatesAdapter(diffUtil : DiffUtil.ItemCallback<ConversionRates>) =
        CurrencyRatesAdapter(diffUtil)
}