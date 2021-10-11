package com.example.food_trock.firebase

import android.util.Log
import com.example.food_trock.TestDB
import com.example.food_trock.models.FoodTruckProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    // Create a instance of Firebase Firestore
    private val mFireStore = FirebaseFirestore.getInstance()


    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }



}