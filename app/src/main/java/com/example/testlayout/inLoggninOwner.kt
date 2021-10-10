package com.example.testlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class inLoggninOwner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_loggnin_owner)


        val intente= Intent(this, editOwnerProfil::class.java)
        startActivity(intente)
    }
}