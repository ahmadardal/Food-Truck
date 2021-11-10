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
import com.example.food_trock.DataManager.currentLat
import com.example.food_trock.DataManager.currentLng
import com.example.food_trock.DataManager.tempStores
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.example.food_trock.fragments.StoreFragment
import com.example.food_trock.adapters.storeAdapter
import com.example.food_trock.utils.MyLocation
import com.google.android.libraries.places.api.Places
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
import java.math.RoundingMode
import java.text.DecimalFormat

class StoreActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView


    lateinit var storeSize: TextView
    val db: FirebaseFirestore = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth

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
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_store)

        pizzaBtn = findViewById(R.id.bt_pizza)
        hotdogBtn = findViewById(R.id.bt_hotdog)
        kebabBtn = findViewById(R.id.bt_kebab)
        sandwhichBtn = findViewById(R.id.bt_Sandwhich)
        japaneseBtn = findViewById(R.id.bt_Japanese)
        vegetarianBtn = findViewById(R.id.bt_vegetarian)
        smoothiesBtn = findViewById(R.id.bt_Smoothie)
        dessertBtn = findViewById(R.id.bt_Dessert)
        storeSize = findViewById(R.id.txtStoreSize)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        storeAdapter = storeAdapter(this, DataManager.stores)
        recyclerView.adapter = storeAdapter
        val loginBtn = findViewById<ImageButton>(R.id.loginBtn)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(1).isChecked = true

        Places.initialize(applicationContext, "AIzaSyBhTEjTc18EJg3UiLW_x8GemJNgu5Ljhdw")
        val myLocation = MyLocation(this, this)
        myLocation.getLocation()
        getStores()

        pizzaBtn.setOnClickListener {
            filter("Pizza",pizzaBtn.isSelected)
            }

        hotdogBtn.setOnClickListener {
            filter("Hotdog",hotdogBtn.isSelected)
        }
        kebabBtn.setOnClickListener {
            filter("Kebab",kebabBtn.isSelected)
        }
        japaneseBtn.setOnClickListener {
            filter("Japanese",japaneseBtn.isSelected)
        }
        sandwhichBtn.setOnClickListener {
            filter("Sandwhiches",sandwhichBtn.isSelected)
        }
        vegetarianBtn.setOnClickListener {
            filter("Vegetarian",vegetarianBtn.isSelected)
        }
        dessertBtn.setOnClickListener {
            filter("Desserts",dessertBtn.isSelected)
        }
        smoothiesBtn.setOnClickListener {
            filter("Smoothies",smoothiesBtn.isSelected)
        }
        swipeRefreshLayout.setOnRefreshListener { 
            getStores()
            swipeRefreshLayout.isRefreshing = false
        }
        loginBtn.setOnClickListener() {
            OpenUserProfile()
        }



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
                bundle.putString("phoneNumber",selectedStore.phoneNumber)
                bundle.putString("openHrs",selectedStore.openHrs)
                bundle.putFloat("rating",selectedStore.storeRating)

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

    /** Queries through the collection-path FoodTrucks in the database to retrieve documents
        If store is online, it is added to the stores list which is displayed in recyclerview
     */

    fun getStores () {
        db.collection("FoodTrucks").get().addOnSuccessListener { task ->
            if (task != null) {
                DataManager.stores.clear()
                tempStores.clear()
                disselectAll()
                for(document in task.documents) {
                    val store = document.toObject(Store::class.java)
                    if(store != null) {
                        if(store.storeStatus) {
                            store.distance = calculateDistance(currentLat, currentLng,store.storeLatitude,store.storeLongitude)
                            DataManager.stores.add(store)
                            tempStores.add(store)
                        } else if (!store.storeStatus) {
                            DataManager.stores.remove(store)
                            tempStores.remove(store)
                        }
                    }
                }
                DataManager.stores.sortBy { it.distance }
                storeSize.text = "Result: ${DataManager.stores.size}"
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    fun filter (selectedTag: String, selected: Boolean) {
        DataManager.stores.clear()
        if(!selected) {
            for (store in tempStores) {
                if (store.category1 == selectedTag || store.category2 == selectedTag) {
                    DataManager.stores.add(store)
                }
            }
        }
        DataManager.stores.sortBy { it.distance }
        selectAndDisselectTags(selectedTag,selected)
        storeSize.text = "Result: ${DataManager.stores.size}"
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun selectAndDisselectTags (selectedTag: String, selected: Boolean) {

        disselectAll()
        if (selected) {
            DataManager.stores.clear()
            for (store in tempStores) {
                DataManager.stores.add(store)
                DataManager.stores.sortBy { it.distance }
            }
        } else {
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

    private fun disselectAll () {
        pizzaBtn.isSelected = false
        hotdogBtn.isSelected = false
        kebabBtn.isSelected = false
        sandwhichBtn.isSelected = false
        japaneseBtn.isSelected = false
        vegetarianBtn.isSelected = false
        dessertBtn.isSelected = false
        smoothiesBtn.isSelected = false
    }

    fun calculateDistance (startLatitude: String,
                             startLongitude: String,
                             endLatitude: Double,
                             endLongitude: Double,
    ):Double {
        try {
            val startLat = startLatitude.toDouble()
            val startLong = startLongitude.toDouble()
            val endLat = endLatitude
            val endLong = endLongitude

            val theta = startLong - endLong
            var dist = Math.sin(deg2rad(startLat)) * Math.sin(deg2rad(endLat)) + Math.cos(deg2rad(startLat)) * Math.cos(deg2rad(endLat)) * Math.cos(deg2rad(theta))
            dist = Math.acos(dist)
            dist = rad2deg(dist)
            dist = dist * 60 * 1.1515
            dist = dist * 1.609344

            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING


            return dist
        }
        catch (e: Exception) {
            return 0.0
        }


    }
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }


}




