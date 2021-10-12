package com.example.food_trock.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.food_trock.R

class inLoggninOwner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_loggnin_owner)


        val intente= Intent(this, editOwnerProfil::class.java)
        startActivity(intente)
    }
}