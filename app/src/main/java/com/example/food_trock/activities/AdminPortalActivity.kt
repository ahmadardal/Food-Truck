package com.example.food_trock.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdminPortalActivity : AppCompatActivity() {
    private lateinit var btnAddNewFT : Button
    private lateinit var btnAddNewAdmin : Button
    private lateinit var btnEnableFT : Button
    private lateinit var btnLogout : Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_portal)
        auth = Firebase.auth
        btnAddNewFT = findViewById(R.id.btn_add_ft)
        btnAddNewAdmin = findViewById(R.id.btn_add_admin)
        btnEnableFT = findViewById(R.id.btn_enable_ft)
        btnLogout = findViewById<Button>(R.id.bt_admin_Logout)

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
        btnLogout.setOnClickListener{
            Log.e("!!!!!!!!!!!!!!!Btn logout","!!!!!!!!!!!!!!!!!!!!!!!!")
            logout()
        }

    }
    fun logout (){
        if(auth.currentUser != null){
            auth.signOut()
            DataManager.currentUserRole.admin = false
            DataManager.currentUserRole.client = false
            DataManager.currentUserRole.foodTruckOwner = false
            val intentLogin = Intent(this@AdminPortalActivity, LoginActivity::class.java)
            startActivity(intentLogin)
        }
    }



}