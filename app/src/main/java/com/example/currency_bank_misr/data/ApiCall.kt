package com.example.currency_bank_misr.data

import retrofit2.http.GET

interface ApiCall {
    @GET("api/v1/employees")
    fun callApi()
}