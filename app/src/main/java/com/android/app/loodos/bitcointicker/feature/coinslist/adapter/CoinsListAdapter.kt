package com.android.app.loodos.bitcointicker.feature.coinslist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.loodos.bitcointicker.R
import com.android.app.loodos.bitcointicker.feature.coinslist.model.CoinsList

class CoinsListAdapter : RecyclerView.Adapter<CoinsListViewHolder>() {

    private  var coinList = mutableListOf<CoinsList>()
    var onItemClick: ((CoinsList) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsListViewHolder {
        return CoinsListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_coin_list,parent,false))
    }

    override fun onBindViewHolder(holder: CoinsListViewHolder, position: Int) {
        holder.bind(coinList[position],onItemClick)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCoinList(coinsList: List<CoinsList>) {
        this.coinList = coinsList.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return coinList.size
    }
}