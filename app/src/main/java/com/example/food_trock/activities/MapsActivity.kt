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
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
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
    lateinit var cardStoreInfo: CardView
    lateinit var cardStoreImage: ImageView
    lateinit var cardStoreName: TextView
    lateinit var cardRating: RatingBar
    lateinit var cardRatingTxt: TextView
    private var firstTime: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getButtonNavigationBar()

        cardStoreInfo = findViewById(R.id.cardStoreInfo)
        cardStoreImage = findViewById(R.id.cardStoreImage)
        cardStoreName = findViewById(R.id.cardStoreName)
        cardRating = findViewById(R.id.cardRatingBar)
        cardRatingTxt = findViewById(R.id.txtCardRating)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
            mMap.setOnMarkerClickListener { marker ->
                for (store in DataManager.stores) {
                    if (store.storeName == marker.tag) {
                        cardStoreInfo.visibility = View.VISIBLE
                        Glide.with(this).load(store.storeImage).into(cardStoreImage)
                        cardStoreName.text = store.storeName
                        cardRating.rating = store.storeRating.toFloat()
                        cardRatingTxt.text = store.storeRating.toString()
                    }
                }
                if (marker.isInfoWindowShown) {

                } else {
                    marker.showInfoWindow()
                }
                true
            }
        mMap.setOnMapClickListener {
            cardStoreInfo.visibility = View.GONE
        }
        getStores()
        if (firstTime) {
            firstTime = false
            addMarker(
                LatLng(DataManager.currentLat.toDouble(), DataManager.currentLng.toDouble()),
                "You are here",
                null
            )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(DataManager.currentLat.toDouble(), DataManager.currentLng.toDouble()), 15f))
        } else {
            //update marker position
        }
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
