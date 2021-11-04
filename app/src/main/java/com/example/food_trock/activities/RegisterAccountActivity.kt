package com.example.food_trock.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.food_trock.R
import com.example.food_trock.firebase.FireStoreClass
import com.example.food_trock.models.Roles
import com.example.food_trock.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterAccountActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog
    lateinit var btn_signUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_account)

        btn_signUp = findViewById(R.id.registerButtonActRA)
        btn_signUp.setOnClickListener {
            registerUser()
        }
    }

    /**
     * A function to register a user to our app using the Firebase.
     */
    private fun registerUser() {
        // Here we get the text from editText and trim the space
        val name: String = findViewById<EditText>(R.id.fullNameActRA).text.toString().trim { it <= ' ' }
        val email: String = findViewById<EditText>(R.id.userNameActRA).text.toString().trim { it <= ' ' }
        val password: String = findViewById<EditText>(R.id.passwordActRA).text.toString().trim { it <= ' ' }
        val passwordConfirmed: String = findViewById<EditText>(R.id.passwordConfirmaion).text.toString().trim { it <= ' ' }
        var assignedRole : Roles = Roles(admin = false, client = true, foodTruckOwner = false)

        if (validateForm(name, email, password, passwordConfirmed)) {
            //اگه فیلد ها خالی نبود شرط داخل کد اجرا میشود
            // Show the progress dialog.
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
                            FireStoreClass().registerUser(this@RegisterAccountActivity, user)
                        } else {
                            Toast.makeText(
                                this@RegisterAccountActivity,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }

    /**
     * A function to validate the entries of a new user.
     */
    private fun validateForm(name: String, email: String, password: String, passwordConfirmed: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                Toast.makeText(applicationContext,"Nåt namn har dina föräldrar väl valt!!!",Toast.LENGTH_LONG)
                false
            }
            TextUtils.isEmpty(email) -> {
                Toast.makeText(applicationContext,"Email/Epost/Epost-adress!!!",Toast.LENGTH_LONG)
                false
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(applicationContext,"Lösenord!!!",Toast.LENGTH_LONG)
                false
            }
            password != passwordConfirmed ->{
                Toast.makeText(applicationContext,"Kom igen! Du borde kunna bekräfte ditt lösenord!!",Toast.LENGTH_LONG)
                false
            }
            else -> {
                true
            }
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
     * This function is used to hide the progress dialog when user is registered.
     */
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun userRegisteredSuccess() {

        Toast.makeText(
            this@RegisterAccountActivity,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()

        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }
}