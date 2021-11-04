package com.example.food_trock.activities

import android.app.Dialog
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.food_trock.R
import com.example.food_trock.firebase.FireStoreClass
import com.example.food_trock.models.Roles
import com.example.food_trock.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AdminAddFoodTruckActivity: AppCompatActivity() {

    private lateinit var btnAddNewFT : Button
    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);    this.getWindow().setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_admin_add_foodtruck)

        btnAddNewFT = findViewById(R.id.btn_add_ft)
        btnAddNewFT.setOnClickListener {
            addNewFoodTruck()
        }
    }

    private fun addNewFoodTruck() {
        // Here we get the text from editText and trim the space
        val name: String = findViewById<EditText>(R.id.et_newFoodTruckName).text.toString().trim { it <= ' ' }
        val email: String = findViewById<EditText>(R.id.et_newFoodTruckEmail).text.toString().trim { it <= ' ' }
        val password: String = findViewById<EditText>(R.id.et_newFoodTruckPassword).text.toString().trim { it <= ' ' }

        var assignedRole : Roles = Roles(admin = false, client = false, foodTruckOwner = true)
        if(validateForm(name, email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!

                            val user = User(
                                firebaseUser.uid, name, registeredEmail, role = assignedRole
                            )

                            // call the registerUser function of FirestoreClass to make an entry in the database.
                            FireStoreClass().registerUser(this@AdminAddFoodTruckActivity, user)
                            sendverificationEmail(name,email, password)
                        } else {
                            Toast.makeText(
                                this@AdminAddFoodTruckActivity,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }

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
     * A function to validate the entries of a new FoodTruck.
     */
    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                Toast.makeText(applicationContext,"FoodTruck borde vara döpt!!!", Toast.LENGTH_LONG)
                false
            }
            TextUtils.isEmpty(email) -> {
                Toast.makeText(applicationContext,"Email/Epost/Epost-adress!!!", Toast.LENGTH_LONG)
                false
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(applicationContext,"Lösenord!!!", Toast.LENGTH_LONG)
                false
            }
            else -> {
                true
            }
        }
    }
    fun sendverificationEmail(name: String, email: String, password: String){

    }

    /**
     * This function is used to hide the progress dialog when user is registered.
     */
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun userRegisteredSuccess() {

        Toast.makeText(
            this@AdminAddFoodTruckActivity,
            "You have successfully registered a new FoodTruck.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()

        // Finish the Sign-Up Screen
        finish()
    }



}