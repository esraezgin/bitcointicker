package com.android.app.loodos.bitcointicker.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseHelper {

    lateinit var auth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var firebaseDB: FirebaseFirestore

    fun getInstance() {
        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!
        firebaseDB=Firebase.firestore

    }

}