package com.example.food_trock.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.View
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.perfmark.Tag
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    //Button VavigationBar
    private lateinit var bottomNavigationView: BottomNavigationView

    // The entry point to the Places API.
    private lateinit var storePlace: Store
    private val REQUEST_LOCATION = 1
    var locationRequest: LocationRequest? = null
    lateinit var locationCallback: LocationCallback
    // The entry point to the Fused Location Provider.
    private lateinit var locationProviderClient: FusedLocationProviderClient
   // lateinit var db: FirebaseFirestore
    //lateinit var auth: FirebaseAuth

    private lateinit var mFromLatLng: LatLng
    private lateinit var mToLatLng: LatLng

    companion object{
       private const val REQUEST_CODE_AUTOCOMPLETE_FROM= 1
       private const val REQUEST_CODE_AUTOCOMPLETE_TO= 2
       private const val TAG= "MapsActivity"
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



       // db = Firebase.firestore
       // auth = Firebase.auth


        locationRequest = createLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d(
                        "!!!",
                        "onLocationResult: Lat: ${location.latitude} Log: ${location.longitude}"
                    )
                }
            }
        }

        getButtonNavigationBar()
        getLocationProvider()
        setupPlaces()

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val stockholm = LatLng(60.0, 18.5)
        addMarker(stockholm, "stockholm")

        //map.moveCamera(CameraUpdateFactory.newLatLng(stockholm))


    }

    fun addMarker(latLng: LatLng, title :String){

        val markerOptions = MarkerOptions().position(latLng).title(title)
       mMap.addMarker(markerOptions)

    }

    private fun setupPlaces(){
        Places.initialize(applicationContext, "AIzaSyBhTEjTc18EJg3UiLW_x8GemJNgu5Ljhdw")

        btn_From.setOnClickListener {
            startAutocomplete(REQUEST_CODE_AUTOCOMPLETE_FROM)
        }

        btn_To.setOnClickListener {
            startAutocomplete(REQUEST_CODE_AUTOCOMPLETE_TO)
        }

       // tv_From.text= "From: {getString(R.string.no_place_selected)}"
       // tv_To.text= "From: {getString(R.string.no_place_selected)}"




    }
    private fun startAutocomplete(requestCode: Int){
        //Fields of place data to return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)


        //Start the autocomplete intent.
        val intent= Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields ).build(this)

        startActivityForResult(intent,requestCode)
    }
    @SuppressLint("StringFormatInvalid")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_FROM) {
            processAutocompleteResult(resultCode, data){ place ->
                tv_From.text = getString(R.string.label_From, place.address)
                place.latLng?.let{
                    mFromLatLng= it
                }
            }

            return

        }else if(requestCode == REQUEST_CODE_AUTOCOMPLETE_TO){

            processAutocompleteResult(resultCode, data) { place ->
                tv_To.text = getString(R.string.label_From, place.address)
                place.latLng?.let{
                    mToLatLng= it
                }
            }
            return

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun processAutocompleteResult(resultCode: Int, data: Intent?,callback: (Place)->Unit ){

        Log.d(TAG, "processAutocompleteResult(resultCode = $resultCode ")
        when (resultCode) {
            Activity.RESULT_OK -> {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    Log.i(TAG, "Place: $place ")
                    callback(place)


                }
            }
            AutocompleteActivity.RESULT_ERROR -> {
                // TODO: Handle the error.
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    status.statusMessage?.let {
                            message-> Log.i(TAG, message)
                    }
                }
            }
        }
        return

    }

//------Fun for Permissions to map--------//
    override fun onResume() {
        super.onResume()
        //startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        //stopLocationUpdates()
    }
    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    }

    fun stopLocationUpdates() {
        locationProviderClient.removeLocationUpdates(locationCallback)
    }
    fun getLocationProvider() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("!!!", "getLocationProvider: permission not granted!")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION
            )

        } else {
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
    fun createLocationRequest(): LocationRequest {
        val request = LocationRequest.create()

        request.interval = 4000
        request.interval = 1000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        return request

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("!!!", "onRequestPermissionsResult: permission grated!")
                startLocationUpdates()

            } else {
                Log.d("!!!", "onRequestPermissionsResult: permission denied!")
            }

        }
    }
    //Permissions ---- End//

//-------Fun for navigatin Bar-------//
    fun getButtonNavigationBar(){
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.menu.getItem(1).isChecked = true

        val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.favourites -> {
                    bottomNavigationView.menu.getItem(1).isChecked = false
                    val intent = Intent(this@MapsActivity, FavouritesActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.trucks -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.settings -> {
                    bottomNavigationView.menu.getItem(1).isChecked = false
                    val intent = Intent(this@MapsActivity, OwnerSettingsActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.maps -> {
                    bottomNavigationView.menu.getItem(1).isChecked = false
                    val intent = Intent(this@MapsActivity, MapsActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false

        }
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

    }

}














