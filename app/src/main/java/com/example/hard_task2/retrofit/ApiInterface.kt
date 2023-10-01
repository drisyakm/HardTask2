package com.example.hard_task2.retrofit

import com.example.hard_task2.model.City
import com.example.hard_task2.model.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("Countries")
    fun getCountries() : Call<List<Country>>

    @GET("States")
    fun getStates(@Query("countryId") countryId:Int) : Call<List<City>>
}