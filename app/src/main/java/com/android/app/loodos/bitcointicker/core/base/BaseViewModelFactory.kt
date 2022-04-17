package com.android.app.loodos.bitcointicker.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.app.loodos.bitcointicker.network.Repository

class BaseViewModelFactory constructor(private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repository::class.java).newInstance(repository)
        }
    }
