package com.example.food_trock.activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import com.example.food_trock.DataManager
import com.example.food_trock.DataManager.tempStores
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
import kotlinx.android.synthetic.main.activity_store.*

class StoreActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView


    lateinit var storeSize: TextView
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    lateinit var kebabBtn: Button
    lateinit var hotdogBtn: Button
    lateinit var dessertBtn: Button
    lateinit var vegetarianBtn: Button
    lateinit var japaneseBtn: Button
    lateinit var pizzaBtn: Button
    lateinit var smoothiesBtn: Button
    lateinit var sandwhichBtn: Button
    lateinit var storeAdapter: storeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE); this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_store)
        pizzaBtn = findViewById(R.id.bt_pizza)
        hotdogBtn = findViewById(R.id.bt_hotdog)
        kebabBtn = findViewById(R.id.bt_kebab)
        sandwhichBtn = findViewById(R.id.bt_Sandwhich)
        japaneseBtn = findViewById(R.id.bt_Japanese)
        vegetarianBtn = findViewById(R.id.bt_vegetarian)
        smoothiesBtn = findViewById(R.id.bt_Smoothie)
        dessertBtn = findViewById(R.id.bt_Dessert)




        pizzaBtn.setOnClickListener {
            filter("Pizza")
        }
        hotdogBtn.setOnClickListener {
            filter("Hotdog")
        }
        kebabBtn.setOnClickListener {
            filter("Kebab")
        }
        japaneseBtn.setOnClickListener {
            filter("Japanese")
        }
        sandwhichBtn.setOnClickListener {
            filter("Sandwhiches")
        }
        vegetarianBtn.setOnClickListener {
            filter("Vegetarian")
        }
        dessertBtn.setOnClickListener {
            filter("Desserts")
        }
        smoothiesBtn.setOnClickListener {
            filter("Smoothies")
        }



        storeSize = findViewById(R.id.txtStoreSize)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        storeAdapter = storeAdapter(this, DataManager.stores)


        recyclerView.adapter = storeAdapter
        val loginBtn = findViewById<ImageButton>(R.id.loginBtn)


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(1).isChecked = true

        db = Firebase.firestore
        auth = Firebase.auth
        val user = auth.currentUser


        /*val user1 = user?.email?.let { User(user!!.uid,"hello","hello2", it,"",0,) }
        if (user1 != null) {
            db.collection("Users").document(user!!.uid).set(user1)
        }

         */

        loginBtn.setOnClickListener() {
            OpenUserProfile()
        }


        /** Queries through the collection-path FoodTrucks in the database to find data changes
         * If store is online, the storelist is cleared and the online stores are added to the recyclerview
         */

        db.collection("FoodTrucks").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                DataManager.stores.clear()
                if (value != null) {
                    for (document in value.documents) {
                        val store = document.toObject(Store::class.java)
                        if (store != null) {
                            if (store.storeStatus) {
                                DataManager.stores.add(store)
                                tempStores.add(store)

                            } else if (!store.storeStatus) {
                                DataManager.stores.remove(store)
                                tempStores.remove(store)
                            }
                        }
                        storeSize.text = "Result: ${DataManager.stores.size}"
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        })

        /*db.collection("FoodTrucks").document(auth.currentUser!!.uid).set(Store("blabla" ,
            "","Rai ",
            10,5,
            false,auth.currentUser!!.uid,
            0.0,0.0,
            "Asian","Vegetarian",true,true,false,false,false,false))*/

        storeAdapter.setOnItemClickListener(object : storeAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val storeFragment = StoreFragment()

                var selectedStore: Store = storeAdapter.storeList[position]
                val bundle = Bundle()
                storeFragment.arguments = bundle
                bundle.putString("storeName", selectedStore.storeName)
                bundle.putInt("storePriceClass", selectedStore.storePriceClass)
                bundle.putString("storeImage", selectedStore.storeImage)
                bundle.putString("storeID", selectedStore.UID)

                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.container, storeFragment, "store")
                transaction.commit()
            }
        })
    }


    val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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
                bottomNavigationView.menu.getItem(1).isChecked = false
                val intent = Intent(this@StoreActivity, MapsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }








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


    fun filter (selectedTag: String) {
        DataManager.stores.clear()
        selectAndDisselectTags(selectedTag)
        for(store in tempStores) {
            if(store.category1 == selectedTag || store.category2 == selectedTag) {
                DataManager.stores.add(store)
            }
        }
        storeSize.text = "Result: ${DataManager.stores.size}"
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun selectAndDisselectTags (selectedTag: String) {
        pizzaBtn.isSelected = false
        hotdogBtn.isSelected = false
        kebabBtn.isSelected = false
        sandwhichBtn.isSelected = false
        japaneseBtn.isSelected = false
        vegetarianBtn.isSelected = false
        dessertBtn.isSelected = false
        smoothiesBtn.isSelected = false

        when (selectedTag) {
            "Pizza" -> pizzaBtn.isSelected = true
            "Hotdog" -> hotdogBtn.isSelected = true
            "Kebab" -> kebabBtn.isSelected = true
            "Sandwhiches" -> sandwhichBtn.isSelected = true
            "Japanese" -> japaneseBtn.isSelected = true
            "Vegetarian" -> vegetarianBtn.isSelected = true
            "Desserts" -> dessertBtn.isSelected = true
            "Smoothies" -> smoothiesBtn.isSelected = true
        }
    }

}




