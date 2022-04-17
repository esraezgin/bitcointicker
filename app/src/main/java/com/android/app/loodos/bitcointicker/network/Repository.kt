package com.android.app.loodos.bitcointicker.network


class Repository constructor(private val api : RetrofitApi) {
     fun getCoinsList() = api.getCoinsList()
}