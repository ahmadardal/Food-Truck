package com.example.food_trock.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.food_trock.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavouritesActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.favourites -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.trucks -> {
                bottomNavigationView.menu.getItem(0).isChecked = false
                val intent = Intent(this@FavouritesActivity, StoreActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.settings -> {
                bottomNavigationView.menu.getItem(0).isChecked = false
                val intent = Intent(this@FavouritesActivity, OwnerSettingsActivity::class.java)
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
        setContentView(R.layout.activity_favourites)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(0).isChecked = true
    }
}