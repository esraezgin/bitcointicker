package com.android.app.loodos.bitcointicker.feature.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.app.loodos.bitcointicker.R
import com.android.app.loodos.bitcointicker.core.base.BaseViewModel
import com.android.app.loodos.bitcointicker.core.base.BaseViewModelFactory
import com.android.app.loodos.bitcointicker.core.common.Helper
import com.android.app.loodos.bitcointicker.databinding.FragmentLoginBinding
import com.android.app.loodos.bitcointicker.network.FirebaseHelper
import com.android.app.loodos.bitcointicker.network.Repository
import com.android.app.loodos.bitcointicker.network.RetrofitApi
import com.google.firebase.auth.FirebaseUser

class LoginFragment : Fragment() {

    private lateinit var viewModel: BaseViewModel
    private lateinit var binding : FragmentLoginBinding
    private val retrofitApi = RetrofitApi.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, BaseViewModelFactory(Repository(retrofitApi))).get(BaseViewModel::class.java)
        FirebaseHelper.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            Helper.setVisibility(true,binding.progressBar)
            signIn()
        }
    }

    private fun signIn() {
        viewModel.userEmail = binding.emailText.text?.trim().toString()
        viewModel.userPassword = binding.passwordText.text?.trim().toString()


        FirebaseHelper.auth.signInWithEmailAndPassword(viewModel.userEmail,viewModel.userPassword)
            .addOnCompleteListener(requireActivity()) {
            if(it.isSuccessful) {
                FirebaseHelper.firebaseUser = FirebaseHelper.auth.currentUser!!
                Helper.setVisibility(false,binding.progressBar)
                findNavController().navigate(R.id.action_loginFragment_to_coinsListFragment)
            }else {
                Toast.makeText(requireContext(),it.exception.toString(),Toast.LENGTH_LONG).show()
                Helper.setVisibility(false,binding.progressBar)
            }
        }
    }
}