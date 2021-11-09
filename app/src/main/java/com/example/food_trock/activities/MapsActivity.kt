package com.example.food_trock.activities

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.databinding.ActivityMapsBinding
import com.example.food_trock.fragments.StoreFragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView


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
            mMap.setOnMarkerClickListener { marker ->
                for (store in DataManager.stores) {
                    if (store.storeName == marker.tag) {
                        Log.e("test", "test")
                        val storeFragment = StoreFragment()
                        val bundle = Bundle()
                        storeFragment.arguments = bundle
                        bundle.putString("storeName", store.storeName)
                        bundle.putInt("storePriceClass", store.storePriceClass)
                        bundle.putString("storeImage", store.storeImage)
                        bundle.putString("storeID", store.UID)

                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.add(R.id.container, storeFragment, "store")
                        transaction.commit()
                    }
                }
                if (marker.isInfoWindowShown) {

                } else {
                    marker.showInfoWindow()
                }
                true
            }
        getStores()
        if (DataManager.currentLng != "" && DataManager.currentLat != null && firstTime) {
            firstTime = false
            addMarker(
                LatLng(DataManager.currentLat.toDouble(), DataManager.currentLng.toDouble()),
                "Your are here",
                null
            , null)
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
                        .into(object : CustomTarget<Bitmap>(200, 200){
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                addMarker(storeLatLng, store.storeName, resource, store.storeName)
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

    private fun getCroppedBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(), (
                    bitmap.width / 4).toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output
    }

    fun addMarker(latLng: LatLng, title :String, image: Bitmap?, tag: String?){
        if (image != null) {
            val Icon: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getCroppedBitmap(image))
            val markerOptions = MarkerOptions().position(latLng).title(title).icon(Icon)

            mMap.addMarker(markerOptions).setTag(tag)
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
