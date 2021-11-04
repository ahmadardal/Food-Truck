package com.example.food_trock.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.firebase.FireStoreClass
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavouritesActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    val userId = FireStoreClass().getCurrentUserID()

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.favourites -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.trucks -> {

                bottomNavigationView.menu.getItem(0).isChecked = false
                val intent = Intent(this@FavouritesActivity, StoreActivity::class.java)
                startActivity(intent)
                DataManager.tempStores.clear()

                return@OnNavigationItemSelectedListener true
            }
            R.id.settings -> {
                bottomNavigationView.menu.getItem(0).isChecked = false
                val intent = Intent(this@FavouritesActivity, OwnerSettingsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.maps -> {
                val intent = Intent(this@FavouritesActivity, MapsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_favourites)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(0).isChecked = true
    }
}