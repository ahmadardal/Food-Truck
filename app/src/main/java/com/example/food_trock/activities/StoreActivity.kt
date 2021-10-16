package com.example.food_trock.activities

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.example.food_trock.fragments.StoreFragment
import com.example.food_trock.adapters.storeAdapter
import com.example.food_trock.models.StoreStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
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

        val user = auth.currentUser
        val store = Store(
            user?.uid, R.drawable.kebab, "Kebab", 50, "10km", 0, false
        )
        val email = "pikachu@test.se"
        val password = "hello123"



/*
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                Log.e("TEST","onCreate: account created")
            } else {
                Log.e("TEST","onCreate: failed")
            }
        }














        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e("TEST", "onCreate: login successful")
            }
        }











        db.collection("FoodTrucks").add(store)






 */



/*
        val storeUID = StoreStatus(store.userID)
        if (user != null) {
            db.collection("users").document(user.uid).collection("truckInfo").add(store)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        db.collection("foodtruckUID").add(storeUID)
                        Log.e("TEST", "onCreate: store added")
                    }
                }

        }

 */



       /* if (user != null) {
            db.collection("users").document(user.uid).collection("truckInfo")
                .addSnapshotListener {snapshot ,e ->
                    if(snapshot != null){
                        for(document in snapshot.documents) {
                            val store = document.toObject(Store::class.java)
                            if (store != null) {
                                db.collection("truckList").add(store)
                            }
                            Log.e("TEST","onCreate:${store}")
                        }
                    }
                }
        }

        */
        /*
        db.collection("users").document("C8p53Hnej2bXMZsJghLjGKlfLWH3").collection("truckInfo")
            .addSnapshotListener(object: EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                DataManager.stores.clear()
                if (value != null) {
                    for(document in value.documents) {
                        val store = document.toObject(Store::class.java)
                        if (store != null) {
                            if (store.storeOnline) {
                                DataManager.stores.add(store)
                            } else if (!store.storeOnline) {
                                DataManager.stores.remove(store)
                            }

                        }
                        recyclerView.adapter?.notifyDataSetChanged()
                    }

                }
            }
        })

         */


        /*
        db.collection("foodtruckUID").addSnapshotListener{snapshot,e ->
            if(snapshot != null) {
                for (document in snapshot.documents) {
                    val storeID = document.toObject(StoreStatus::class.java)
                    if (storeID != null) {
                        DataManager.listOfUID.add(storeID)
                        cardUpdater()
                    }
                }
            }
        }

         */




        db.collection("FoodTrucks").addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                DataManager.stores.clear()
                if (value != null) {
                    for(document in value.documents) {
                        val store = document.toObject(Store::class.java)
                        if(store != null) {
                            if(store.storeOnline) {
                                DataManager.stores.add(store)
                            } else if (!store.storeOnline) {
                                DataManager.stores.remove(store)
                            }
                        }
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        })


    }
    /*
    fun cardUpdater () {
        for (storeID in DataManager.listOfUID) {
            storeID.storeUID?.let { db.collection("users").document(it) }
                ?.collection("truckInfo")
                ?.addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        DataManager.stores.clear()
                        if (value != null) {
                            for (document in value.documents) {
                                val store = document.toObject(Store::class.java)
                                if (store != null) {
                                    if (store.storeOnline) {
                                        DataManager.stores.add(store)
                                    } else if (!store.storeOnline) {
                                        DataManager.stores.remove(store)
                                    }
                                }
                                recyclerView.adapter?.notifyDataSetChanged()
                            }
                        }
                    }
                })
        }
    }

     */
}















