package com.example.hard_task2.retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.example.hard_task2.BuildConfig
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    fun getClient() : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val requestToApiInterceptor= Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .build()

            var request = chain.request()
                .newBuilder()
                .build()

            return@Interceptor chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(requestToApiInterceptor)
            .build()

        val builder = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(builder))
            .build()
        return retrofit

    }

    fun<T> buildService(service: Class<T>): T{
        return getClient().create(service)
    }
}