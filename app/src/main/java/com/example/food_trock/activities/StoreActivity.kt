package com.example.food_trock.activities

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.example.food_trock.fragments.StoreFragment
import com.example.food_trock.adapters.storeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StoreActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var storeAdapter = storeAdapter(this, DataManager.stores)
        recyclerView.adapter = storeAdapter

        db = Firebase.firestore
        auth = Firebase.auth

        storeAdapter.setOnItemClickListener(object : storeAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {


                val storeFragment = StoreFragment()

                var selectedStore: Store = storeAdapter.storeList[position]
                val bundle = Bundle()
                storeFragment.arguments = bundle
                bundle.putString("storeName", selectedStore.storeName)
                bundle.putInt("storePriceClass", selectedStore.storePriceClass)
                bundle.putString("storeDistance", selectedStore.storeDistance)


                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.container, storeFragment, "store")
                transaction.commit()




            }
        })

        val raisStore = Store(R.drawable.hamburger,"Rai Pizzeria", 100, "10km",)
        val email = "rai@test.se"
        val password = "123456"
        /*
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if(result.isSuccessful) {
                Log.e("TEST","onCreateUser: successful")
            } else {
                Log.e("TEST", "onCreateUser: unsuccessful")
            }
        }

 */
        /*
        val user = auth.currentUser
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if(result.isSuccessful) {
                Log.e("TEST","onLogin: successful user: ${user!!.email}")

            }
        }

         */

        /*
        if (user != null) {
            db.collection("users").document(user.uid).collection("truckInfo").add(raisStore)
        }
         */




    }
}