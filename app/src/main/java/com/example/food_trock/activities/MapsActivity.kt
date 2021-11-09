package com.example.food_trock.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.food_trock.DataManager
import com.example.food_trock.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private var firstTime: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(com.example.food_trock.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getButtonNavigationBar()


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getStores()
        if (DataManager.currentLng != "" && DataManager.currentLat != null && firstTime) {
            firstTime = false
            addMarker(
                LatLng(DataManager.currentLat.toDouble(), DataManager.currentLng.toDouble()),
                "Your are here",
                null
            )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(DataManager.currentLat.toDouble(), DataManager.currentLng.toDouble()), 15f))
        } else {
            //update marker position
        }
    }

    fun addCustomMarker(context: Context, resource: Bitmap?, _name: String?): Bitmap {
        val marker: View =
            (context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                com.example.food_trock.R.layout.custom_marker_layout,
                null
            )
        val markerImage = marker.findViewById(com.example.food_trock.R.id.user_dp) as CircleImageView
       // markerImage.setImageResource(resource)
        markerImage.setImageBitmap(resource)
        val txt_name = marker.findViewById(com.example.food_trock.R.id.name) as TextView
        txt_name.text = _name
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.setLayoutParams(ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT))
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            marker.getMeasuredWidth(),
            marker.getMeasuredHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)

        return bitmap;

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
            val Icon: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(addCustomMarker(this, image, title))
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
        bottomNavigationView = findViewById(com.example.food_trock.R.id.bottom_navigation)
        bottomNavigationView.menu.getItem(2).isChecked = true

        val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                com.example.food_trock.R.id.favourites -> {
                    bottomNavigationView.menu.getItem(2).isChecked = false
                    val intent = Intent(this@MapsActivity, FavouritesActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                com.example.food_trock.R.id.trucks -> {
                    bottomNavigationView.menu.getItem(2).isChecked = false
                    val intent = Intent(this@MapsActivity, StoreActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                com.example.food_trock.R.id.settings -> {
                    bottomNavigationView.menu.getItem(2).isChecked = false
                    val intent = Intent(this@MapsActivity, OwnerSettingsActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                com.example.food_trock.R.id.maps -> {
                    return@OnNavigationItemSelectedListener false
                }
            }
            false

        }
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

    }
}
