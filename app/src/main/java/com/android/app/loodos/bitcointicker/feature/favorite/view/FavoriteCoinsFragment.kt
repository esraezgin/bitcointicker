package com.android.app.loodos.bitcointicker.feature.favorite.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.app.loodos.bitcointicker.R
import com.android.app.loodos.bitcointicker.core.base.BaseViewModel
import com.android.app.loodos.bitcointicker.core.base.BaseViewModelFactory
import com.android.app.loodos.bitcointicker.core.common.Helper
import com.android.app.loodos.bitcointicker.databinding.FragmentfavoriteBinding
import com.android.app.loodos.bitcointicker.feature.coinslist.adapter.CoinsListAdapter
import com.android.app.loodos.bitcointicker.feature.coinslist.model.CoinsList
import com.android.app.loodos.bitcointicker.feature.coinslist.view.CoinListFragment
import com.android.app.loodos.bitcointicker.network.FirebaseHelper
import com.android.app.loodos.bitcointicker.network.Repository
import com.android.app.loodos.bitcointicker.network.RetrofitApi
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FavoriteCoinsFragment : Fragment() {

    private lateinit var viewModel: BaseViewModel
    private lateinit var binding: FragmentfavoriteBinding
    private lateinit var adapter : CoinsListAdapter

    private val retrofitApi = RetrofitApi.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentfavoriteBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, BaseViewModelFactory(Repository(retrofitApi))).get(
            BaseViewModel::class.java
        )

        FirebaseHelper.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView() {
        adapter = CoinsListAdapter()
         val  bundle = Bundle()
        binding.recyclerCoinList.adapter = adapter
        binding.toolbar.tvToolbarTitle.text = getString(R.string.favorite_coin)
        val dbRef=FirebaseHelper.firebaseDB.collection("fav_coins").document(FirebaseHelper.firebaseUser.uid)

        Helper.setVisibility(true,binding.progressBar)
        dbRef.get().addOnSuccessListener {
            val favoriteCoins =it.get("coins") as ArrayList<HashMap<String,String>>
            val sendList= mutableListOf<CoinsList>()
            favoriteCoins.forEach {
                sendList.add(CoinsList(it["id"],it["symbol"],it["name"]))
            }
            Helper.setVisibility(false,binding.progressBar)
            adapter.setCoinList(sendList)
        }
        adapter.onItemClick = { item ->
            bundle.putSerializable(CoinListFragment.SELECTED_COIN_ITEM_SYMBOL,item)
            bundle.putBoolean(FROM_FAVORITE,true)
            findNavController().navigate(R.id.action_favoriteCoinsFragment_to_coinsDetailFragment,bundle)
        }
    }

    companion object {
        const val FROM_FAVORITE="FROM_FAVORITE"
    }
}