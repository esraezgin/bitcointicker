package com.android.app.loodos.bitcointicker.feature.coinslist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.app.loodos.bitcointicker.R
import com.android.app.loodos.bitcointicker.databinding.ActivityCoinslistBinding
import com.android.app.loodos.bitcointicker.feature.coinslist.viewmodel.CoinsListViewModel
import com.android.app.loodos.bitcointicker.factory.BaseViewModelFactory
import com.android.app.loodos.bitcointicker.feature.coinslist.adapter.CoinsListAdapter
import com.android.app.loodos.bitcointicker.network.RetrofitApi
import com.android.app.loodos.bitcointicker.repository.Repository
import com.android.app.loodos.bitcointicker.utils.Helper


class CoinsListActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinsListViewModel
    private lateinit var binding : ActivityCoinslistBinding

    lateinit  var adapter : CoinsListAdapter
    private val retrofitApi = RetrofitApi.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinslistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, BaseViewModelFactory(Repository(retrofitApi))).get(CoinsListViewModel::class.java)
        viewModel.getCoinsList()
        initObserve()
        initView()
        editSearchView()
    }

    private fun editSearchView() {
        binding.edSearchingView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.setCoinList(viewModel.filterCoinList(query))
            }

            override fun afterTextChanged(p0: Editable?)  = Unit

        })

        binding.ivClearSearchView.setOnClickListener {
            binding.edSearchingView.text?.clear()
        }
    }

    private fun initObserve() {
         viewModel.coinList.observe(this, Observer {
             if(it.isEmpty()){
                 binding.progressBar.visibility = View.VISIBLE
             }else {
                 binding.progressBar.visibility = View.GONE
                 binding.recyclerCoinList.visibility = View.VISIBLE
                 adapter.setCoinList(it)
                 viewModel.originalCoinList = it
                 binding.recyclerCoinList.adapter = adapter
             }

        })
    }

    private fun initView() {
        adapter = CoinsListAdapter()
        binding.recyclerCoinList.layoutManager = LinearLayoutManager(this)
        binding.recyclerCoinList.hasFixedSize()
        Helper.setToolbarTitle(getString(R.string.app_name), binding.toolbar.tvToolbarTitle)
        binding.toolbar.ivToolbarFavoriteIcon.visibility = View.VISIBLE
    }



}