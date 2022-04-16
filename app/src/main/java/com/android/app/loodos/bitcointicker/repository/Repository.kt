package com.android.app.loodos.bitcointicker.repository

import com.android.app.loodos.bitcointicker.network.RetrofitApi

class Repository constructor(private val api : RetrofitApi) {

     fun getCoinsList() = api.getCoinsList()
}