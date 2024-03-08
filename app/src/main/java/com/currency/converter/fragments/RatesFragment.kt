package com.currency.converter.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.currency.converter.adapter.CurrencyRatesAdapter
import com.currency.converter.databinding.FragmentRatesBinding
import com.currency.converter.model.ConversionRates
import com.currency.converter.remote.NetworkResult
import com.currency.converter.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RatesFragment : Fragment() {

    private lateinit var binding: FragmentRatesBinding
    private val mainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var adapter : CurrencyRatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRatesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextCurrency.imeOptions = EditorInfo.IME_ACTION_GO

        binding.editTextCurrency.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                val eurAmount = binding.editTextCurrency.text.toString()
                if (eurAmount.isNotBlank()) {
                    binding.progressbar.isVisible = true
                    val eur = eurAmount.toDouble()
                    mainViewModel.getExchangeRates()
                    mainViewModel.response.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is NetworkResult.Success -> {
                                // bind data to the view
                                binding.progressbar.isVisible = false
                                val conversionRates = response.data?.conversionRates
                                if (conversionRates != null) {
                                    // Convert map entries to a list
                                    val conversionList = conversionRates.values.toList()
                                    val currenciesList = conversionRates.keys.toList()
                                    val conversionRatesList = arrayListOf<ConversionRates>()
                                    for (i in 1 until currenciesList.size) {
                                        val currencyCode = currenciesList[i]
                                        val currencyRate = conversionList[i]
                                        val conversionRate = currencyRate * eur
                                        conversionRatesList.add(
                                            ConversionRates(
                                                id = i,
                                                currencyCode = currencyCode,
                                                currencyRate = currencyRate,
                                                conversionRate = conversionRate
                                            )
                                        )
                                    }
                                    binding.currencyRatesRV.layoutManager = LinearLayoutManager(requireContext())
                                    binding.currencyRatesRV.adapter = adapter
                                    adapter.submitList(conversionRatesList)
                                }
                            }

                            is NetworkResult.Error -> {
                                // show error message
                                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                                Log.d("currency", "error: ${response.message}")
                            }

                            is NetworkResult.Loading -> {
                                // show a progress bar
                            }
                        }
                    }
                }
            }
            false
        }
    }
}