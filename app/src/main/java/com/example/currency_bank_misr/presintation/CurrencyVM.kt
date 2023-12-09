package com.example.currency_bank_misr.presintation

import androidx.lifecycle.ViewModel
import com.example.currency_bank_misr.data.currency.CurrencyRepo
import javax.inject.Inject

class CurrencyVM @Inject constructor(private val repository: CurrencyRepo) : ViewModel()  {
}