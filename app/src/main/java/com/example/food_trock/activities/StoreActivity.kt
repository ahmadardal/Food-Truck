package com.example.food_trock.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.example.food_trock.fragments.StoreFragment
import com.example.food_trock.adapters.storeAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    private lateinit var bottomNavigationView: BottomNavigationView

    lateinit var storeSize: TextView
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE); this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_store)


        storeSize = findViewById(R.id.txtStoreSize)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var storeAdapter = storeAdapter(this, DataManager.stores)
        recyclerView.adapter = storeAdapter
        val search = findViewById<EditText>(R.id.searchView)
        val loginBtn = findViewById<ImageButton>(R.id.loginBtn)


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(1).isChecked = true

        db = Firebase.firestore
        auth = Firebase.auth
        val user = auth.currentUser

        loginBtn.setOnClickListener() {
            OpenUserProfile()
        }
        
        

        /** Queries through the collection-path FoodTrucks in the database to find data changes
         * If store is online, the storelist is cleared and the online stores are added to the recyclerview
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
                        storeSize.text = "Result: ${DataManager.stores.size}"
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        })

        storeAdapter.setOnItemClickListener(object : storeAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {


                val storeFragment = StoreFragment()

                var selectedStore: Store = storeAdapter.storeList[position]
                val bundle = Bundle()
                storeFragment.arguments = bundle
                bundle.putString("storeName", selectedStore.storeName)
                bundle.putInt("storePriceClass", selectedStore.storePriceClass)
                bundle.putString("storeDistance", selectedStore.storeDistance)
                bundle.putString("storeImage", selectedStore.storeImage)


                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.container, storeFragment, "store")
                transaction.commit()




            }
        })
    }


    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.favourites -> {
                bottomNavigationView.menu.getItem(1).isChecked = false
                val intent = Intent(this@StoreActivity, FavouritesActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.trucks -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.settings -> {
                bottomNavigationView.menu.getItem(1).isChecked = false
                val intent = Intent(this@StoreActivity, OwnerSettingsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.maps -> {
/*                val intent = Intent(this@MainActivity, MyRecipes::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true*/
            }
        }
        false

    }

    val store = Store(
        "ok", "Burgers", 50, "10km", 0, false
    )


        /*
        if (user != null) {
            db.collection("FoodTrucks").document(user.uid).set(store)
        }

         */




    /**
     * Opens the user profile, if signed in. Else opens the login activity.
     */
    fun OpenUserProfile() {
        if (auth.currentUser != null) {
            val intent = Intent(this, OwnerSettingsActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
