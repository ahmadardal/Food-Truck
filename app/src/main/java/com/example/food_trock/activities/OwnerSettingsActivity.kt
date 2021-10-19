package com.example.food_trock.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var bottomNavigationView: BottomNavigationView

class OwnerSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_settings)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(2).isChecked = true
    }

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.trucks -> {
                bottomNavigationView.menu.getItem(2).isChecked = false
                val intent = Intent(this, StoreActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.settings -> {
                item.isChecked = true
                return@OnNavigationItemSelectedListener false
            }
            R.id.maps -> {
/*                val intent = Intent(this@MainActivity, MyRecipes::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true*/
            }
        }
        false

    }
}