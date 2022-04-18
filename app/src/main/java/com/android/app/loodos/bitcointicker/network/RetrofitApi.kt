package com.android.app.loodos.bitcointicker.network

import com.android.app.loodos.bitcointicker.feature.coinslist.model.CoinsList
import com.android.app.loodos.bitcointicker.core.common.Constants
import com.android.app.loodos.bitcointicker.feature.detailcoins.model.CoinDetails
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RetrofitApi {

     @GET("coins/list")
     fun getCoinsList():  Call<List<CoinsList>>

    @GET("coins/{id}")
     fun getCoinDetails(@Path("id")  id:String,
                     @Query("localization") localization:Boolean,
                     @Query("tickers") tickers:Boolean,
                     @Query("market_data") market_data:Boolean,
                     @Query("community_data") community_data:Boolean,
                     @Query("developer_data") developer_data:Boolean,
                     @Query("sparkline") sparkline:Boolean):Call<CoinDetails>


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