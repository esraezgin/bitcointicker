package com.android.app.loodos.bitcointicker.feature.detailcoins.model

import com.google.gson.annotations.SerializedName

data class CoinDetails(
    @SerializedName("id") val id: String? = null,
    @SerializedName("symbol") val symbol: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("hashing_algorithm") val hashing_algorithm: String? = null,
    @SerializedName("description") val description: Description? = null,
    @SerializedName("image") val image: Image? = null,
    @SerializedName("market_data") val market_data: MarketData? = null,
)

data class Description(
    @SerializedName("en") val en: String? = null
)
data class Image(
    @SerializedName("thumb") val thumb: String? = null,
    @SerializedName("small") val small: String? = null,
)
data class MarketData(
    @SerializedName("current_price") val current_price: CurrentPrice? = null,
    @SerializedName("price_change_percentage_24h") val price_change_percentage_24h: Double? = null,
)

data class CurrentPrice(
    @SerializedName("usd") val usd: Double? = null,
)


