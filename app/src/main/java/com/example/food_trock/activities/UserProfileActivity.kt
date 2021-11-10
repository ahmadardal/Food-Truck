package com.example.food_trock.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.fragments.MenuListFragment
import com.example.food_trock.models.Store
import com.example.food_trock.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_owner_settings.*
import kotlinx.android.synthetic.main.activity_userprofile.*

private lateinit var bottomNavigationView: BottomNavigationView

class UserProfileActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var editFullName: EditText
    lateinit var editPhoneNumber: EditText
    lateinit var ownerProfileIMG: ImageView
    lateinit var txtEmail: TextView
    lateinit var txtMyName: TextView
    var selectedPhotoUri: Uri? = null
    val userCollectionRef = Firebase.firestore.collection("Users")



    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.favourites -> {
                bottomNavigationView.menu.getItem(3).isChecked = false
                val intent = Intent(this, FavouritesActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.trucks -> {
                bottomNavigationView.menu.getItem(3).isChecked = false
                val intent = Intent(this, StoreActivity::class.java)
                startActivity(intent)
                DataManager.tempStores.clear()

                return@OnNavigationItemSelectedListener true
            }
            R.id.settings -> {
                item.isChecked = true
                return@OnNavigationItemSelectedListener false
            }
            R.id.maps -> {
                val intent = Intent(this@UserProfileActivity, MapsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_userprofile)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(3).isChecked = true

        val saveChangesBtn = findViewById<Button>(R.id.saveChangesBtn)
        val logOutBTN = findViewById<Button>(R.id.bt_Logout)
        db = Firebase.firestore
        auth = Firebase.auth

        editFullName = findViewById(R.id.editUserFullName)
        editPhoneNumber = findViewById(R.id.editUserPhone)
        txtEmail = findViewById(R.id.txtEmail)
        ownerProfileIMG = findViewById(R.id.ownerProfileImage)
        txtMyName = findViewById(R.id.txtMyName)




        setUserInfo()



        /** Calls uploadImageToFirebaseStorage function which in turn calls getNewStoreMap.
         */

        saveChangesBtn2.setOnClickListener() {
            getNewStoreMap()
        }



        /**
         * Starts the intent to pick an image from gallery.
         */
        ownerProfileIMG.setOnClickListener() {
            Log.e("TEST", "photo clicked")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        logOutBTN.setOnClickListener {
            logOut()
        }



    }

    /**
     * Turns the selected photo into a bitmap and sets the image.
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.e("TEST","onActivityResult: photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            ownerProfileIMG.setImageBitmap(bitmap)



            uploadImageToFirebaseStorage()
        }
    }

    /**
     * Creates a new store map based on info from editTexts and takes in a profileImageURL
     * sets the map into database and merges it leaving unchanged values as they were.
     */
    private fun getNewStoreMap (profileImageURL: String = "") {
        val fullName = editFullName.text.toString()
        val phoneNumber = editPhoneNumber.text.toString()
        val profilePicture = profileImageURL
        val map = mutableMapOf<String, Any>()
        if (profilePicture.isNotEmpty()) {
            map["image"] = profilePicture
        }
        if (fullName.isNotEmpty()) {
            map["fullName"] = fullName
        }
        if (phoneNumber.isNotEmpty()) {
            map["mobile"] = phoneNumber
        }
        auth.currentUser?.let {
            userCollectionRef.document(it.uid).set(map, SetOptions.merge())
        }
    }



    /**
     * Generates a filename with random UUID and loads it into /images.
     * selectedPhotoUri gets placed under the filename and the url is downloaded and sent to
     * getNewStoreMap() for mapping.
     */
    private fun uploadImageToFirebaseStorage() {

        if(selectedPhotoUri != null) {
            //val filename = UUID.randomUUID().toString()
            val filename = auth.currentUser?.uid
            val imagesRef = FirebaseStorage.getInstance().getReference("/images/$filename")

            imagesRef.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.e("TEST", "onUpload: successfully uploaded image: ${it.metadata?.path}")

                    imagesRef.downloadUrl.addOnSuccessListener {
                        it.toString()
                        Log.e("TEST", "onUpload: FileLocation: $it")

                        getNewStoreMap(it.toString())

                    }
                }
        }
    }

    private fun setUserInfo () {
        auth.currentUser?.let {
            userCollectionRef.document(it.uid).get().addOnSuccessListener { result ->
                if (result != null) {
                    val user : User? = result.toObject(User::class.java)
                    if (user != null) {
                        Glide.with(this)
                            .load(user.image)
                            .into(ownerProfileIMG)
                        editFullName.setText(user.fullName)
                        txtEmail.text = auth.currentUser!!.email
                        editPhoneNumber.setText(user.mobile.toString())
                        txtMyName.text = user.fullName

                    }
                }
            }
        }
    }

    /**
     * Logs the truck owner out of the app. Returns to login activity.
     */
    fun logOut(){
        if(auth.currentUser != null){
            auth.signOut()
            val intent= Intent(this, LoginActivity::class.java )
            startActivity(intent)
        }

    }
}