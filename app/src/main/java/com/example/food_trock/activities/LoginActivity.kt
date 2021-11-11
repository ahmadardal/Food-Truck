package com.example.food_trock.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.firebase.FireStoreClass
import com.example.food_trock.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var mProgressDialog: Dialog
    private lateinit var btnLogin: Button
    private lateinit var homeBtn: ImageButton
    private lateinit var createAccount: TextView
    val db: FirebaseFirestore = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alternative_loginscreen)

        btnLogin = findViewById(R.id.btn_login)
        btnLogin.setOnClickListener {
            signInRegisteredUser()
        }
        homeBtn.setOnClickListener() {
            val intent = Intent(this,StoreActivity::class.java)
            startActivity(intent)
        }

        createAccount = findViewById(R.id.txtSignUp)
        createAccount.setOnClickListener() {
            val intent = Intent(this, RegisterAccountActivity::class.java)
            startActivity(intent)
        }
    }


    /**
     * A function for Sign-In using the registered user using the email and password.
     */
    private fun signInRegisteredUser() {

        // Here we get the text from editText and trim the space
        val email: String = findViewById<EditText>(R.id.editCreateEmail).text.toString().trim { it <= ' ' }
        val password: String = findViewById<EditText>(R.id.editPassword).text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Calling the FirestoreClass signInUser function to get the data of user from database.
                        FireStoreClass().loadUserData(this@LoginActivity)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    /**
     * A function to validate the entries of a user.
     */
    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext,"Enter a valid email",Toast.LENGTH_LONG)
            false
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext,"Password cannot be empty",Toast.LENGTH_LONG)
            false
        } else {
            true
        }
    }

    /**
     * A function to get the user details from the firestore database after authentication.
     */
    fun signInSuccess(user: User) {

        hideProgressDialog()

        val intentAdmin = Intent(this@LoginActivity, AdminPortalActivity::class.java)
        val intentClient = Intent(this@LoginActivity, UserProfileActivity::class.java)
        val intentTruckOwner = Intent(this@LoginActivity, OwnerSettingsActivity::class.java)


        if(DataManager.currentUserRole.admin) {
            startActivity(intentAdmin)
        } else if (DataManager.currentUserRole.foodTruckOwner) {
            startActivity(intentTruckOwner)
        } else {
            startActivity(intentClient)
        }

        this.finish()
    }


    /**
     * This function is used to show the progress dialog with the title and message to user.
     */
    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.findViewById<TextView>(R.id.tv_progress_text).text = text

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }
    /**
     * This function is used to hide the progress dialog when user is registered.
     */
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }



}