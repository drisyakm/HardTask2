package com.example.hard_task2.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hard_task2.model.City
import com.example.hard_task2.model.Country
import com.example.hard_task2.retrofit.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(private val apiInterface: ApiInterface,private val sharedPreferences: SharedPreferences): ViewModel() {

    val countryLiveData: MutableLiveData<List<Country>> by lazy {
        MutableLiveData<List<Country>>()
    }
    val cityLiveData: MutableLiveData<List<City>> by lazy {
        MutableLiveData<List<City>>()
    }

    fun getCountryList(){
        apiInterface.getCountries().enqueue(object: Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if(response.isSuccessful && response.body()!=null){
                    countryLiveData.postValue(response.body())
                }else{
                    countryLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
               countryLiveData.postValue(null)
            }

        })
    }

    fun getStateList(countryId:Int){
        apiInterface.getStates(countryId).enqueue(object: Callback<List<City>> {
            override fun onResponse(call: Call<List<City>>, response: Response<List<City>>) {
                if(response.isSuccessful && response.body()!=null){
                    cityLiveData.postValue(response.body())
                }else{
                    cityLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<City>>, t: Throwable) {
                cityLiveData.postValue(null)
            }

        })
    }
   fun setLanguage(lang:String){
       sharedPreferences.edit().putString("lang", lang).apply()
   }
   fun getLanguage(): String {
       return sharedPreferences.getString("lang", "en")?:"en"
   }
}