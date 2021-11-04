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
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places


class MainActivity : AppCompatActivity() {
    private lateinit var locationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        Places.initialize(applicationContext, "AIzaSyBhTEjTc18EJg3UiLW_x8GemJNgu5Ljhdw")

        var intent = Intent(this, StoreActivity::class.java)
        startActivity(intent)
        onGPS()

    }

    private fun isLocationEnabled() : Boolean {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun onGPS() {
        if (!isLocationEnabled()) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 200)
            return
        } else {
            requestLocation()
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun requestLocation() {

        val requestLocation = LocationRequest()
        requestLocation.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        requestLocation.interval = 50
        requestLocation.fastestInterval = 50

        locationProviderClient.requestLocationUpdates(
            requestLocation, callback, Looper.myLooper()
        )
    }

    val callback = object: LocationCallback() {
        override fun onLocationAvailability(result: LocationAvailability) {
            super.onLocationAvailability(result)
        }

        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)

            val lastLocation = result?.lastLocation
            DataManager.currentLat = lastLocation.latitude.toString()
            DataManager.currentLng = lastLocation.longitude.toString()
        }
    }
}