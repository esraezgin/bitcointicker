package com.android.app.loodos.bitcointicker.core.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.app.loodos.bitcointicker.core.common.Helper
import com.android.app.loodos.bitcointicker.feature.coinslist.model.CoinsList
import com.android.app.loodos.bitcointicker.feature.detailcoins.model.CoinDetails
import com.android.app.loodos.bitcointicker.network.FirebaseHelper
import com.android.app.loodos.bitcointicker.network.Repository
import com.android.app.loodos.bitcointicker.network.RetrofitApi
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BaseViewModel constructor(private val repository: Repository) : ViewModel() {


    private lateinit var coin: HashMap<String, String>
    lateinit var favoriteCoinList: MutableList<CoinsList>
    lateinit var dbRef:DocumentReference

    var originalCoinList: List<CoinsList> = emptyList()
    var selectedCoinItem: CoinsList? = null

    var fromFavorite: Boolean = false

    var userEmail: String = ""
    var userPassword: String = ""

    var coinList = MutableLiveData<List<CoinsList>>()
    var coinDetailLiveData = MutableLiveData<CoinDetails>()


    fun getCoinsList() {
        val response = repository.getCoinsList()
        response.enqueue(object : Callback<List<CoinsList>> {
            override fun onResponse(
                call: Call<List<CoinsList>>,
                response: Response<List<CoinsList>>
            ) {
                if (response.isSuccessful) {
                    coinList.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<CoinsList>>, t: Throwable) {
                Log.e(
                    BaseViewModel::class.java.name,
                    t.localizedMessage?.toString() ?: t.message.toString()
                )
            }
        })
    }

    fun setDocumentReference(){
        dbRef =FirebaseHelper.firebaseDB.collection("fav_coins").document(FirebaseHelper.firebaseUser.uid)
    }

    fun getCoinDetails(id: String) {
        RetrofitApi.getInstance().getCoinDetails(
            id = id,
            localization = false,
            tickers = false,
            market_data = true,
            community_data = false,
            developer_data = false,
            sparkline = false
        ).enqueue(object : Callback<CoinDetails> {
            override fun onResponse(call: Call<CoinDetails>, response: Response<CoinDetails>) {
                if (response.isSuccessful) {
                    coinDetailLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<CoinDetails>, t: Throwable) {
                Log.e(
                    BaseViewModel::class.java.name + "-detail",
                    t.localizedMessage?.toString() ?: t.message.toString()
                )
            }

        })
    }

    fun filterCoinList(query: CharSequence?): List<CoinsList> {
        return if (!query.isNullOrEmpty()) {
            originalCoinList.filter {
                it.symbol?.lowercase()?.contains(query.toString().lowercase().trim())!! ||
                        it.name?.lowercase()?.contains(query.toString().lowercase().trim())!!
            }
        } else {
            originalCoinList
        }
    }

    fun updateFavoriteCoinList() {
        coin = hashMapOf(
            "id" to selectedCoinItem?.id!!,
            "symbol" to selectedCoinItem?.symbol!!,
            "name" to selectedCoinItem?.name!!
        )
        dbRef.update("coins", FieldValue.arrayUnion(coin))
    }

    fun setFavoriteCoinList(documentSnapshot: DocumentSnapshot) {
        val favoriteCoins = documentSnapshot.get("coins") as ArrayList<HashMap<String, String>>
        favoriteCoinList = mutableListOf<CoinsList>()
        favoriteCoins.forEach { item ->
            favoriteCoinList.add(CoinsList(item["id"], item["symbol"], item["name"]))
        }
        favoriteCoinList.reverse()
    }

}