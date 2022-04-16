package com.android.app.loodos.bitcointicker.network

import com.android.app.loodos.bitcointicker.feature.coinslist.model.CoinsList
import com.android.app.loodos.bitcointicker.utils.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface RetrofitApi {

     @GET("coins/list")
     fun getCoinsList():  Call<List<CoinsList>>


    companion object {
        var retrofitApi: RetrofitApi? = null

        fun getInstance() : RetrofitApi {
            if (retrofitApi == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.apiURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitApi = retrofit.create(RetrofitApi::class.java)
            }
            return retrofitApi!!

        }
        }
}