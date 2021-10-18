package com.example.food_trock.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.squareup.okhttp.Dispatcher


class OwnerSettingsActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var editTruckName: EditText
    lateinit var editTruckPrice: EditText
    lateinit var ownerProfileIMG: ImageView
    lateinit var txtStatus: TextView
    val foodTruckCollectionRef = Firebase.firestore.collection("FoodTrucks")


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


    // Creates a new store map based on info from editTexts, returns the map.
    private fun getNewStoreMap (): Map<String, Any> {
        val truckName = editTruckName.text.toString()
        val truckPrice = editTruckPrice.text.toString()
        val map = mutableMapOf<String, Any>()
        if(truckName.isNotEmpty()) {
            map["storeName"] = truckName
        }
        if(truckPrice.isNotEmpty()) {
            map["storePriceClass"] = truckPrice.toInt()
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

    private fun logOut(){
        if(auth.currentUser != null){
            auth.signOut()
            val intent= Intent(this, LoginActivity::class.java )
            startActivity(intent)
        }

    }

}