package com.example.food_trock


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.example.food_trock.activities.MapsActivity
import com.example.food_trock.activities.OwnerSettingsActivity
import com.example.food_trock.activities.SplashActivity
import com.example.food_trock.activities.StoreActivity
import com.example.food_trock.utils.MyLocation
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Places.initialize(applicationContext, "AIzaSyBhTEjTc18EJg3UiLW_x8GemJNgu5Ljhdw")

        val myLocation = MyLocation(this, this)
        myLocation.getLocation()

        val intent = Intent(this, StoreActivity::class.java)
        startActivity(intent)

    }
}