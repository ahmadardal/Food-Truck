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
import com.example.food_trock.R
import com.example.food_trock.models.Store
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

        var saveChangesBtn = findViewById<Button>(R.id.saveChangesBtn)
        var logOutBTN= findViewById<Button>(R.id.bt_Logout)
        var switchBtn = findViewById<Switch>(R.id.switchBtn)
        txtStatus = findViewById(R.id.txtStatus)
        db = Firebase.firestore
        auth = Firebase.auth

        editTruckName = findViewById(R.id.editTruckName)
        editTruckPrice = findViewById(R.id.editTruckPrice)
        ownerProfileIMG = findViewById(R.id.ownerProfileImage)





        //Checks if the owners shop is online or offline. Sets the switch status.
        auth.currentUser?.let { foodTruckCollectionRef.document(it.uid).get().addOnSuccessListener { result ->
            if(result != null) {
                val store = result.toObject(Store::class.java)
                if (store != null) {
                    if(store.storeOnline) {
                        switchBtn.isChecked = true
                    }
                }
            }
        } }


        // Saves the new truck map and updates database, other values are untouched.
        saveChangesBtn.setOnClickListener() {
            /*val oldTruck = getOldStoreInfo()*/
            uploadImageToFirebaseStorage()
            val newTruckMap = getNewStoreMap()
            updateStore(/*oldTruck,*/newTruckMap)
        }

        // Checks and unchecks the switch button.
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


/*
    private fun getOldStoreInfo (): Store {
        var oldStore = Store()
        foodTruckCollectionRef.addSnapshotListener{snapshot,e ->
            if(snapshot != null) {
                for (document in snapshot.documents) {
                    val oldStoreInfo = document.toObject(Store::class.java)
                    if(oldStoreInfo!= null && oldStoreInfo.userID == auth.currentUser?.uid) {
                        oldStore = oldStoreInfo
                    }

                }
            }
        }
        return oldStore
    }

 */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.e("TEST","onActivityResult: photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            ownerProfileIMG.setImageDrawable(bitmapDrawable)
        }
    }

    // Creates a new store map based on info from editTexts, returns the map.
    private fun getNewStoreMap (profileImageURL: String = ""): Map<String, Any> {
        val truckName = editTruckName.text.toString()
        val truckPrice = editTruckPrice.text.toString()
        val truckImage = profileImageURL
        val map = mutableMapOf<String, Any>()
        if(truckName.isNotEmpty()) {
            map["storeName"] = truckName
        }
        if(truckPrice.isNotEmpty()) {
            map["storePriceClass"] = truckPrice.toInt()
        }
        if(truckImage.isNotEmpty()) {
            map["storeImage"] = truckImage
        }
        return map
    }

    // Sets the newTruckMap, does not replace untouched values.
    private fun updateStore (/*truck: Store,*/newTruckMap: Map<String, Any>) {
        auth.currentUser?.let { foodTruckCollectionRef.document(it.uid).set(newTruckMap, SetOptions.merge()) }


    }

    private fun requestLocationPermission () {
        // TODO: 2021-10-18 Ask for location permission and set the current location of truck
        // TODO: Function is placed inside switch status button.
    }

    private fun uploadImageToFirebaseStorage() {

        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val imagesRef = FirebaseStorage.getInstance().getReference("/images/$filename")

        imagesRef.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.e("TEST","onUpload: successfully uploaded image: ${it.metadata?.path}")

                imagesRef.downloadUrl.addOnSuccessListener {
                    it.toString()
                    Log.e("TEST","onUpload: FileLocation: $it")

                    getNewStoreMap(it.toString())

                }
            }

    }

    fun logOut(){
        if(auth.currentUser != null){
            auth.signOut()
            val intent= Intent(this, LoginActivity::class.java )
            startActivity(intent)
        }

    }

}