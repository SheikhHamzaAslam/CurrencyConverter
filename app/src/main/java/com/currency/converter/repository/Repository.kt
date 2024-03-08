package com.currency.converter.repository

import com.currency.converter.remote.NetworkResult
import com.currency.converter.remote.RemoteDataSource
import com.currency.converter.retrofit.BaseApiResponse
import com.currency.converter.model.ExchangeRate
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
        private val remoteDataSource: RemoteDataSource
    ) : BaseApiResponse() {
    suspend fun getExchangeRates(): Flow<NetworkResult<ExchangeRate>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getExchangeRates() })
        }.flowOn(Dispatchers.IO)
    }
}