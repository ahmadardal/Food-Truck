package com.example.food_trock


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.food_trock.activities.MapsActivity
import com.example.food_trock.activities.OwnerSettingsActivity
import com.example.food_trock.activities.SplashActivity
import com.example.food_trock.activities.StoreActivity



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent = Intent(this, StoreActivity::class.java)
        startActivity(intent)

    }
}