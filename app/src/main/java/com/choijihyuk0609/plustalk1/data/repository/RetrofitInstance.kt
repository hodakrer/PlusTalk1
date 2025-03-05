package com.choijihyuk0609.plustalk1.data.repository

import com.choijihyuk0609.plustalk1.network.NetworkInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder( )
            .baseUrl("https://rapapa.site/")
            .addConverterFactory(GsonConverterFactory.create( ) )
            .build( )
    }
    val apiService: NetworkInterface by lazy {
        retrofit.create(NetworkInterface::class.java)
    }
}