package com.example.food_trock.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.food_trock.databinding.ActivityMapsBinding
import com.example.food_trock.models.Store
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.View
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var locationProviderClient: FusedLocationProviderClient
   // lateinit var db: FirebaseFirestore
    //lateinit var auth: FirebaseAuth

    lateinit var currentLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getButtonNavigationBar()
        Places.initialize(applicationContext, "AIzaSyBhTEjTc18EJg3UiLW_x8GemJNgu5Ljhdw")

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        onGPS()
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

    fun getStores() {
        for(store in DataManager.stores) {
            if(store != null) {
                if(store.storeOnline) {
                    val storeLatLng = LatLng(store.storeLatitude, store.storeLongitude)
                    addMarker(storeLatLng, store.storeName)
                }
            }

        }
    }


    val callback = object:LocationCallback() {
        override fun onLocationAvailability(result: LocationAvailability) {
            super.onLocationAvailability(result)
        }

        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)

            val lastLocation = result?.lastLocation

            currentLocation = LatLng(lastLocation.latitude, lastLocation.longitude)

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
            addMarker(currentLocation, "myLocation")
        }
    }

    private fun isLocationEnabled() : Boolean {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun addMarker(latLng: LatLng, title :String){

        val markerOptions = MarkerOptions().position(latLng).title(title)
       mMap.addMarker(markerOptions)

    }

    override fun onResume() {
        super.onResume()
        onGPS()
    }

    fun getButtonNavigationBar(){
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.menu.getItem(2).isChecked = true

        val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.favourites -> {
                    bottomNavigationView.menu.getItem(2).isChecked = false
                    val intent = Intent(this@MapsActivity, FavouritesActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.trucks -> {
                    bottomNavigationView.menu.getItem(2).isChecked = false
                    val intent = Intent(this@MapsActivity, StoreActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.settings -> {
                    bottomNavigationView.menu.getItem(2).isChecked = false
                    val intent = Intent(this@MapsActivity, OwnerSettingsActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.maps -> {
                    return@OnNavigationItemSelectedListener false
                }
            }
            false

        }
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

    }
}
