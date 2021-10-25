package com.example.food_trock.activities

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.core.View

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    // The entry point to the Places API.
    private lateinit var storePlace: Store

    private val REQUEST_LOCATION = 1
    var locationRequest : LocationRequest? = null
    lateinit var locationCallback: LocationCallback


    // The entry point to the Fused Location Provider.
    private lateinit var locationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        getLocationProvider()
        locationRequest=createLocationRequest()

        locationCallback= object :LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                for(location in locationResult.locations){
                    Log.d("!!!", "onLocationResult: Lat: ${location.latitude} Log: ${location.longitude}")
                }
            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap




    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }


    fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED){
            locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper() )
        }

    }
    fun stopLocationUpdates(){
        locationProviderClient.removeLocationUpdates(locationCallback)
    }

     fun getLocationProvider(){
        locationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
            Log.d("!!!", "getLocationProvider: permission not granted!")
            ActivityCompat.requestPermissions( this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_LOCATION)

        }else{
            Log.d("!!!", "getLocationProvider: permission is already granted!")
            /*locationProviderClient.lastLocation.addOnSuccessListener { location ->
                if(location != null) {
                    val lat= location.latitude
                    val lng= location.longitude
                    Log.d("!!!", "getLocationProvider: lat: ${lat}, lon: ${lng}")

                }
            }*/


        }
    }

    fun createLocationRequest() : LocationRequest{
        val request = LocationRequest.create()

        request.interval= 4000
        request.interval= 1000
        request.priority= LocationRequest.PRIORITY_HIGH_ACCURACY

        return request

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("!!!", "onRequestPermissionsResult: permission grated!")
                startLocationUpdates()

            }else{
                Log.d("!!!", "onRequestPermissionsResult: permission denied!")
            }

        }
    }
}











