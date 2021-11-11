package com.example.food_trock


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.food_trock.utils.MyLocation
import com.google.android.libraries.places.api.Places


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Places.initialize(applicationContext, "AIzaSyBhTEjTc18EJg3UiLW_x8GemJNgu5Ljhdw")
        val myLocation = MyLocation(this, this)
        myLocation.getLocation()

        Log.e("!!!","main")

    }
}