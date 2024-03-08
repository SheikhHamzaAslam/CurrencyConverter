package com.currency.converter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.currency.converter.repository.Repository
import com.currency.converter.remote.NetworkResult
import com.currency.converter.model.ExchangeRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResult<ExchangeRate>> = MutableLiveData()
    val response: LiveData<NetworkResult<ExchangeRate>> = _response

    fun getExchangeRates() = viewModelScope.launch {
        repository.getExchangeRates().collect { values ->
            _response.value = values
        }
    }
}