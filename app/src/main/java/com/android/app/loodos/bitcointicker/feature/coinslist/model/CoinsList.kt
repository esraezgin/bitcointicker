package com.android.app.loodos.bitcointicker.feature.coinslist.model

import com.google.gson.annotations.SerializedName

data class CoinsList(
    @SerializedName("id")  val id: String? = null,
    @SerializedName("symbol")  val symbol: String? = null,
    @SerializedName("name")  val name: String? = null,
)
