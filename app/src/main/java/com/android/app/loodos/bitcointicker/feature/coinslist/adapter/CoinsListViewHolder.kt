package com.android.app.loodos.bitcointicker.feature.coinslist.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.android.app.loodos.bitcointicker.R
import com.android.app.loodos.bitcointicker.feature.coinslist.model.CoinsList


class CoinsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun bind(item: CoinsList){
            val coinSymbol = itemView.findViewById(R.id.tv_coin_list_symbol) as AppCompatTextView
            val coinName =  itemView.findViewById(R.id.tv_coin_list_name) as AppCompatTextView

            coinName.text = item.name
            coinSymbol.text = item.symbol
        }
}