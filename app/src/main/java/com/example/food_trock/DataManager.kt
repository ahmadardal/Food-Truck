package com.example.food_trock

import com.example.food_trock.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DataManager {

    val stores = mutableListOf<Store>()

    val menus = mutableListOf<MenuItem>()

    val favorites = mutableListOf<Store>()
    val tempStores = mutableListOf<Store>()
    var currentLat: String = ""
    var currentLng: String = ""
    var currentUserId: String = ""
    var currentUserRole: Roles = Roles(false,false,false)

    init {
        val db: FirebaseFirestore = Firebase.firestore
        val auth: FirebaseAuth = Firebase.auth

        if (auth.currentUser != null) {
            db.collection("Users").document(auth.currentUser!!.uid).get().addOnSuccessListener { task ->
                if(task != null) {
                    val user = task.toObject(User::class.java)
                    if (user != null) {
                        currentUserRole = user.role
                    }
                }
            }
        }

    }
}