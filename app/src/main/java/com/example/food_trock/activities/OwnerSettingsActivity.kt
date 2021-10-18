package com.example.food_trock.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OwnerSettingsActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var editTruckName: EditText
    lateinit var editTruckPrice: EditText
    lateinit var ownerProfileIMG: ImageView
    val foodTruckCollectionRef = Firebase.firestore.collection("FoodTrucks")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_owner_settings)

        var btnSaveChanges = findViewById<Button>(R.id.saveChangesBtn)
        db = Firebase.firestore
        auth = Firebase.auth

        editTruckName = findViewById(R.id.editTruckName)
        editTruckPrice = findViewById(R.id.editTruckPrice)


        btnSaveChanges.setOnClickListener() {
            /*val oldTruck = getOldStoreInfo()*/
            val newTruckMap = getNewStoreMap()
            updateStore(/*oldTruck,*/newTruckMap)
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

    private fun updateStore (/*truck: Store,*/newTruckMap: Map<String, Any>) {
        foodTruckCollectionRef.addSnapshotListener {snapshot,e ->
            if(snapshot != null) {
                for(document in snapshot.documents) {
                    val store = document.toObject(Store::class.java)
                    if(store != null && store.userID == auth.currentUser?.uid) {
                        Log.e("TEST","onUpdateStore: DocumentID: ${document.id}")
                        db.collection("FoodTrucks").document(document.id)
                            .set(newTruckMap, SetOptions.merge())
                    }
                }
            }
        }
    }

}