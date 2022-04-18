package com.android.app.loodos.bitcointicker.feature.favorite.view

import android.os.Bundle
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FavoriteCoinsFragment : Fragment() {

    private lateinit var viewModel: BaseViewModel
    private lateinit var binding: FragmentfavoriteBinding
    private lateinit var adapter : CoinsListAdapter
    private lateinit var bundle : Bundle

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
        bundle = Bundle()

        FirebaseHelper.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView() {
        adapter = CoinsListAdapter()

        binding.recyclerCoinList.adapter = adapter
        binding.toolbar.tvToolbarTitle.text = getString(R.string.favorite_coin)

        viewModel.setDocumentReference()

        Helper.setVisibility(true,binding.progressBar)

        viewModel.dbRef.get().addOnSuccessListener {
            viewModel.setFavoriteCoinList(it)
            Helper.setVisibility(false,binding.progressBar)
            adapter.setCoinList(viewModel.favoriteCoinList)
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