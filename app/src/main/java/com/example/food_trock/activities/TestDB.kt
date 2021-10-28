package com.example.food_trock.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.food_trock.R
import com.example.food_trock.models.FoodTruckProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.time.Instant

class TestDB : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_db)

        // Create a instance of Firebase Firestore
        val mFireStore = FirebaseFirestore.getInstance()

        val getDataBtn = findViewById<Button>(R.id.btn_get_data)
        val postDataBtn = findViewById<Button>(R.id.btn_post_data)
        val showResultTV = findViewById<TextView>(R.id.tv_card_output)

        /**
         * A function to make an entry of the registered foodtruck in the firestore database.
         */
        fun registerFoodTruck(foodTruckInfo: FoodTruckProfile) {

            mFireStore.collection("FoodTruckProfile").document()
                // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
                .set(foodTruckInfo, SetOptions.merge())
                .addOnSuccessListener {

                    // Here Show success msg
                    showResultTV.text = "Data skickat" +
                            "Name: ${foodTruckInfo.name}" +
                            "Description: ${foodTruckInfo.description}" +
                            "Description keyWord: ${foodTruckInfo.descriptionKeyWord}" +
                            "Country: ${foodTruckInfo.country}" +
                            "PhoneNr: ${foodTruckInfo.phoneNr}"

                }
                .addOnFailureListener { e ->

                    showResultTV.text = "Failed to post data, sorry"
                    Log.e(
                        this.javaClass.simpleName,
                        "Error writing document",
                        e
                    )
                }
        }

        /**
         * A function to get FoodTruck data from Firestore Database.
         * Not complete yet, have to test itr
         */
        fun loadFoodTruckData() {

            // Here we pass the collection name from which we wants the data.
            mFireStore.collection("FoodTruckProfile")
                // The document id to get the Fields of user.
                .document()
                .get()
                .addOnSuccessListener { document ->
                    Log.e(this.javaClass.simpleName, document.toString())

                    // Here we have received the document snapshot which is converted into the User Data model object.
                    val gottenFoodTruckData = document.toObject(FoodTruckProfile::class.java)!!
                    // Here Show sucess msg
                    showResultTV.text = "Data skickat" +
                            "Name: ${gottenFoodTruckData.name}" +
                            "Description: ${gottenFoodTruckData.description}" +
                            "Description keyWord: ${gottenFoodTruckData.descriptionKeyWord}" +
                            "Country: ${gottenFoodTruckData.country}" +
                            "PhoneNr: ${gottenFoodTruckData.phoneNr}"
                }
                .addOnFailureListener { e ->
                    showResultTV.text = "Failed to get data, sorry"
                    // Here call a function of base activity for transferring the result to it.
                    Log.e(
                        this.javaClass.simpleName,
                        "Error while getting loggedIn user details",
                        e
                    )
                }
        }


        getDataBtn.setOnClickListener{
            val foodTruckProfileMockData = FoodTruckProfile(
                name = "Harakiri, ${Instant.now().toString()}",
                description = "Stark krov, ${Instant.now().toString()}",
                descriptionKeyWord = "Key words, ${Instant.now().toString()}",
                phoneNr = 123456789,
                country = "Sweden"
            )
            registerFoodTruck(foodTruckProfileMockData)
        }

        postDataBtn.setOnClickListener{

        }



    }
}