package com.android.app.loodos.bitcointicker.feature.detailcoins.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.app.loodos.bitcointicker.core.base.BaseViewModel
import com.android.app.loodos.bitcointicker.core.base.BaseViewModelFactory
import com.android.app.loodos.bitcointicker.core.common.Helper
import com.android.app.loodos.bitcointicker.databinding.FragmentDetailCoinsBinding
import com.android.app.loodos.bitcointicker.feature.coinslist.model.CoinsList
import com.android.app.loodos.bitcointicker.feature.coinslist.view.CoinListFragment
import com.android.app.loodos.bitcointicker.feature.detailcoins.model.CoinDetails
import com.android.app.loodos.bitcointicker.feature.favorite.view.FavoriteCoinsFragment
import com.android.app.loodos.bitcointicker.network.FirebaseHelper
import com.android.app.loodos.bitcointicker.network.Repository
import com.android.app.loodos.bitcointicker.network.RetrofitApi
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldValue

class DetailCoinsFragment : Fragment() {

    private lateinit var viewModel: BaseViewModel
    private lateinit var binding : FragmentDetailCoinsBinding
    private lateinit var coin : HashMap<String,String>


    private val retrofitApi = RetrofitApi.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailCoinsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, BaseViewModelFactory(Repository(retrofitApi))).get(
            BaseViewModel::class.java)

        FirebaseHelper.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedCoinItem = arguments?.getSerializable(CoinListFragment.SELECTED_COIN_ITEM_SYMBOL) as CoinsList
        viewModel.fromFavorite = arguments?.getBoolean(FavoriteCoinsFragment.FROM_FAVORITE) as Boolean

        viewModel.getCoinDetails(viewModel.selectedCoinItem?.id ?: "")
        initObserve()
        initView()

    }

    private fun initView()   {
        Helper.setToolbarTitle(viewModel.selectedCoinItem?.symbol ?: "",binding.toolbar.tvToolbarTitle)

        binding.btnAddFavorite.setOnClickListener {
            coin = hashMapOf(
                "id" to viewModel.selectedCoinItem?.id!!,
                "symbol" to viewModel.selectedCoinItem?.symbol!!,
                "name" to viewModel.selectedCoinItem?.name!!
            )
            val dbRef=FirebaseHelper.firebaseDB.collection("fav_coins").document(FirebaseHelper.firebaseUser.uid)
            dbRef.update("coins",FieldValue.arrayUnion(coin))
            Toast.makeText(requireContext(),"Successful favorite coins ",Toast.LENGTH_LONG).show()
        }

        if(viewModel.fromFavorite){
            binding.btnAddFavorite.visibility=View.GONE
        }else {
            binding.btnAddFavorite.visibility=View.VISIBLE
        }
    }

    private fun initObserve() {
        viewModel.coinDetailLiveData.observe(viewLifecycleOwner) {
            setDetailItem(it)
        }
    }

    private fun setDetailItem(item: CoinDetails) {
        binding.tvCoinDetailCurrentPrice.text = String.format("%.5f", item.market_data?.current_price?.usd)
        binding.tvCoinDetailDescription.text = item.description?.en ?: ""
        binding.tvCoinDetailPriceChangePercentage.text = "%"+Helper.controlDoubleData(item.market_data?.price_change_percentage_24h).toString()
        binding.tvCoinDetailHashingAlgorithm.text = Helper.controlStringData(item.hashing_algorithm)
        if(!item.image?.thumb.isNullOrEmpty()){
            Helper.setVisibility(true,binding.toolbar.ivToolbarFavoriteIcon)
            Glide.with(requireActivity()).load(item.image?.thumb).centerCrop().into(binding.toolbar.ivToolbarFavoriteIcon)
        }

    }


}