package com.android.app.loodos.bitcointicker.feature.coinslist.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.app.loodos.bitcointicker.feature.coinslist.model.CoinsList
import com.android.app.loodos.bitcointicker.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CoinsListViewModel  constructor(private val repository : Repository)  : ViewModel() {

     var coinList = MutableLiveData<List<CoinsList>>()
      var originalCoinList : List<CoinsList> = emptyList()

     fun getCoinsList () {
        val response = repository.getCoinsList()
         response.enqueue(object : Callback<List<CoinsList>> {
             override fun onResponse(call: Call<List<CoinsList>>, response: Response<List<CoinsList>>) {
                if(response.isSuccessful){
                    coinList.postValue(response.body())
                }
             }
             override fun onFailure(call: Call<List<CoinsList>>, t: Throwable) {
                 Log.e(CoinsListViewModel::class.java.name, t.localizedMessage?.toString() ?: t.message.toString())
             }
         })
    }

    fun filterCoinList(query: CharSequence?):List<CoinsList> {
        return if(!query.isNullOrEmpty()) {
            originalCoinList.filter {
                it.name?.lowercase()?.contains(query.toString().lowercase())!! ||
                        it.symbol?.lowercase()?.contains(query.toString().lowercase())!!
            }
        }else {
            originalCoinList
        }
    }

}