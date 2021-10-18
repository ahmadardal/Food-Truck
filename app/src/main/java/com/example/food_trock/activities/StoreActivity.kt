package com.example.food_trock.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.collection.LLRBNode

class StoreActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView


    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.trucks -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.settings -> {
                item.isChecked = true
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var storeAdapter = storeAdapter(this, DataManager.stores)
        recyclerView.adapter = storeAdapter

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(0).isCheckable = false





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


                Log.e("TEST", "TEST")


                var storeName: String = selectedStore.storeName
                var storePriceClass: Int = selectedStore.storePriceClass
                var storeDistance: String = selectedStore.storeDistance


            }
        })
    }
}