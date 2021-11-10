package com.example.food_trock.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.example.food_trock.R

class AdminPortalActivity : AppCompatActivity() {
    private lateinit var btnAddNewFT : Button
    private lateinit var btnAddNewAdmin : Button
    private lateinit var btnEnableFT : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_portal)

        btnAddNewFT = findViewById(R.id.btn_add_ft)
        btnAddNewAdmin = findViewById(R.id.btn_add_admin)
        btnEnableFT = findViewById(R.id.btn_enable_ft)

        btnAddNewFT.setOnClickListener {
            val intent = Intent(this, AdminAddFoodTruckActivity::class.java)
            startActivity(intent)
        }
        btnAddNewAdmin.setOnClickListener {
            val intent = Intent(this, AddNewAdminActivity::class.java)
            startActivity(intent)
        }
        btnEnableFT.setOnClickListener {
            /*val intent = Intent(this, AdminAddFoodTruckActivity::class.java)
            startActivity(intent)*/
        }

        Log.e("!!!","portal")
    }



}