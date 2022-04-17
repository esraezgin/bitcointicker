package com.android.app.loodos.bitcointicker.feature.coinslist.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.app.loodos.bitcointicker.R
import com.android.app.loodos.bitcointicker.databinding.FragmentCoinListBinding
import com.android.app.loodos.bitcointicker.core.base.BaseViewModel
import com.android.app.loodos.bitcointicker.core.base.BaseViewModelFactory
import com.android.app.loodos.bitcointicker.feature.coinslist.adapter.CoinsListAdapter
import com.android.app.loodos.bitcointicker.network.RetrofitApi
import com.android.app.loodos.bitcointicker.network.Repository
import com.android.app.loodos.bitcointicker.core.common.Helper


class CoinListFragment: Fragment(), View.OnClickListener {

    private lateinit var viewModel: BaseViewModel
    private lateinit var binding : FragmentCoinListBinding
    private lateinit var  bundle :Bundle

    lateinit  var adapter : CoinsListAdapter
    private val retrofitApi = RetrofitApi.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCoinListBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, BaseViewModelFactory(Repository(retrofitApi))).get(
            BaseViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initView()
        handleTextWatcher()
        viewModel.getCoinsList()
    }

    private fun handleTextWatcher() {
        binding.edSearchingView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(viewModel.filterCoinList(query).isEmpty()){
                   Helper.setVisibility(true,binding.tvEmptyListWarning)
                }else {
                    Helper.setVisibility(false,binding.tvEmptyListWarning)
                }
                adapter.setCoinList(viewModel.filterCoinList(query))
            }
            override fun afterTextChanged(p0: Editable?)  = Unit
        })
    }

    private fun initObserve() {
         viewModel.coinList.observe(viewLifecycleOwner) {
             if (it.isEmpty()) {
                 Helper.setVisibility(true,binding.progressBar)
             } else {
                 Helper.setVisibility(false,binding.progressBar)
                 Helper.setVisibility(true,binding.recyclerCoinList)
                 adapter.setCoinList(it)
                 viewModel.originalCoinList = it
             }

         }
    }

    private fun initView() {
        adapter = CoinsListAdapter()
        bundle = Bundle()
        binding.recyclerCoinList.adapter = adapter

        binding.ivClearSearchView.setOnClickListener(this)

        Helper.setToolbarTitle(getString(R.string.app_name), binding.toolbar.tvToolbarTitle)
        Helper.setVisibility(true,binding.toolbar.ivToolbarFavoriteIcon)


        adapter.onItemClick = { item ->
            bundle.putSerializable(SELECTED_COIN_ITEM_SYMBOL,item)
            findNavController().navigate(R.id.toCoinsDetailFragment,bundle)
        }

    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.iv_clear_search_view ->  {
                binding.edSearchingView.text?.clear()
            }
        }
    }

    companion object {
        const val  SELECTED_COIN_ITEM_SYMBOL = "SELECTED_COIN_ITEM"
    }


}