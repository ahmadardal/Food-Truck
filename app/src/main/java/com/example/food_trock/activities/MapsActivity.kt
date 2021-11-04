package com.example.food_trock.activities

import android.R.attr.height
import android.R.attr.width
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private var firstTime: Boolean = false

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
                if(store.storeStatus) {
                    val storeLatLng = LatLng(store.storeLatitude, store.storeLongitude)

                    Glide.with(this)
                        .asBitmap()
                        .load(store.storeImage)
                        .fitCenter()
                        .into(object : CustomTarget<Bitmap>(100, 100){
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                addMarker(storeLatLng, store.storeName, resource)
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {
                                // this is called when imageView is cleared on lifecycle call or for
                                // some other reason.
                                // if you are referencing the bitmap somewhere else too other than this imageView
                                // clear it here as you can no longer have the bitmap
                            }
                        })


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

            addMarker(currentLocation, "myLocation", null)
            getStores()

            if (!firstTime) {
                firstTime = true
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
            }
        }
    }

    private fun isLocationEnabled() : Boolean {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun addMarker(latLng: LatLng, title :String, image: Bitmap?){
        if (image != null) {
            val Icon: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(image)
            val markerOptions = MarkerOptions().position(latLng).title(title).icon(Icon)
            mMap.addMarker(markerOptions)
        } else {
            val markerOptions = MarkerOptions().position(latLng).title(title)
            mMap.addMarker(markerOptions)
        }
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
