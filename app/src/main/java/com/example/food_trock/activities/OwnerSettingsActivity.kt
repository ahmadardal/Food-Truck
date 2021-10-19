package com.example.food_trock.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.bumptech.glide.Glide
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.google.android.material.bottomnavigation.BottomNavigationView

private lateinit var bottomNavigationView: BottomNavigationView

class OwnerSettingsActivity : AppCompatActivity() {

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.okhttp.Dispatcher
import java.util.*


class OwnerSettingsActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var editTruckName: EditText
    lateinit var editTruckPrice: EditText
    lateinit var ownerProfileIMG: ImageView
    lateinit var txtStatus: TextView
    val foodTruckCollectionRef = Firebase.firestore.collection("FoodTrucks")
    var selectedPhotoUri: Uri? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_owner_settings)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(2).isChecked = true
    }

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.trucks -> {
                bottomNavigationView.menu.getItem(2).isChecked = false
                val intent = Intent(this, StoreActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.settings -> {
                item.isChecked = true
                return@OnNavigationItemSelectedListener false
            }
            R.id.maps -> {
/*                val intent = Intent(this@MainActivity, MyRecipes::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true*/
            }
        }
        false

        var saveChangesBtn = findViewById<Button>(R.id.saveChangesBtn)
        var logOutBTN= findViewById<Button>(R.id.bt_Logout)
        var switchBtn = findViewById<Switch>(R.id.switchBtn)
        txtStatus = findViewById(R.id.txtStatus)
        db = Firebase.firestore
        auth = Firebase.auth

        editTruckName = findViewById(R.id.editTruckName)
        editTruckPrice = findViewById(R.id.editTruckPrice)
        ownerProfileIMG = findViewById(R.id.ownerProfileImage)


        /** Checks if the owners shop is online or offline.
         * Presets the switch status as well as profile picture
        */

        auth.currentUser?.let { foodTruckCollectionRef.document(it.uid).get().addOnSuccessListener { result ->
            if(result != null) {
                val store = result.toObject(Store::class.java)
                if (store != null) {
                    if(store.storeOnline) {
                        switchBtn.isChecked = true
                    }
                    Glide.with(this)
                        .load(store.storeImage)
                        .into(ownerProfileIMG)
                }
            }
        } }


        /** Calls uploadImageToFirebaseStorage function which in turn calls getNewStoreMap.
         */
        saveChangesBtn.setOnClickListener() {
            /*val oldTruck = getOldStoreInfo()*/
            uploadImageToFirebaseStorage()
        }

        /** Checks and unchecks the switch button. If checked, uncheck and display offline vice versa.
         */
        switchBtn.setOnCheckedChangeListener { _, isChecked ->
            when (switchBtn.isChecked) {
                true -> {
                    txtStatus.text = "Online"
                    txtStatus.setTextColor(Color.parseColor("#FF5EC538"))
                    auth.currentUser?.let { foodTruckCollectionRef.document(it.uid).update("storeOnline", true) }
                }
                false -> {
                    txtStatus.text = "Offline"
                    txtStatus.setTextColor(Color.parseColor("#BCBABA"))
                    auth.currentUser?.let { foodTruckCollectionRef.document(it.uid).update("storeOnline", false) }
                }
            }


        }


        /**
         * Starts the intent to pick an image from gallery.
         */
        ownerProfileIMG.setOnClickListener() {
            Log.e("TEST","photo clicked")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        logOutBTN.setOnClickListener {
            logOut()
        }
    }


    /**
     * Turns the selected photo into a bitmap and sets the image.
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.e("TEST","onActivityResult: photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            ownerProfileIMG.setImageBitmap(bitmap)
        }
    }

    /**
     * Creates a new store map based on info from editTexts and takes in a profileImageURL
     * sets the map into database and merges it leaving unchanged values as they were.
     */
    private fun getNewStoreMap (profileImageURL: String = "") {
        val truckName = editTruckName.text.toString()
        val truckPrice = editTruckPrice.text.toString()
        val truckImage = profileImageURL
        val map = mutableMapOf<String, Any>()
        if (truckName.isNotEmpty()) {
            map["storeName"] = truckName
        }
        if (truckPrice.isNotEmpty()) {
            map["storePriceClass"] = truckPrice.toInt()
        }
        if (truckImage.isNotEmpty()) {
            map["storeImage"] = truckImage
        }
        auth.currentUser?.let {
            foodTruckCollectionRef.document(it.uid).set(map, SetOptions.merge())
        }
    }


    private fun requestLocationPermission () {
        // TODO: 2021-10-18 Ask for location permission and set the current location of truck
        // TODO: Function is placed inside switch status button.
    }


    /**
     * Generates a filename with random UUID and loads it into /images.
     * selectedPhotoUri gets placed under the filename and the url is downloaded and sent to
     * getNewStoreMap() for mapping.
     */
    private fun uploadImageToFirebaseStorage() {

        if(selectedPhotoUri != null) {
            val filename = UUID.randomUUID().toString()
            val imagesRef = FirebaseStorage.getInstance().getReference("/images/$filename")

            imagesRef.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.e("TEST", "onUpload: successfully uploaded image: ${it.metadata?.path}")

                    imagesRef.downloadUrl.addOnSuccessListener {
                        it.toString()
                        Log.e("TEST", "onUpload: FileLocation: $it")

                        getNewStoreMap(it.toString())

                    }
                }
        }
    }

    /**
     * Logs the truck owner out of the app. Returns to login activity.
     */
    fun logOut(){
        if(auth.currentUser != null){
            auth.signOut()
            val intent= Intent(this, LoginActivity::class.java )
            startActivity(intent)
        }

    }

}