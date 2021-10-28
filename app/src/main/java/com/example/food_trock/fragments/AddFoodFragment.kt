package com.example.food_trock.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddFoodFragment: Fragment() {

    private lateinit var editItemName: EditText
    private lateinit var editItemPrice: EditText
    private lateinit var foodImage: ImageView
    private lateinit var txtItemCount: TextView
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var selectedPhotoUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_addmenu2, container, false,)

        editItemName = view.findViewById(R.id.editItemName)
        editItemPrice = view.findViewById(R.id.editItemPrice)
        foodImage = view.findViewById(R.id.addImage)
        txtItemCount = view.findViewById(R.id.txtItemCount)
        db = Firebase.firestore
        auth = Firebase.auth
        val returnBtn = view.findViewById<ImageButton>(R.id.returnBtn)
        val saveChangesBtn = view.findViewById<Button>(R.id.actionBtn)

        txtItemCount.text = "You can add a maximum of 10 items\nYou have ${DataManager.menus.size} items"

        saveChangesBtn.setOnClickListener() {
            uploadImageToFirebaseStorage()
            removeFragment()
        }

        returnBtn.setOnClickListener() {
            removeFragment()
        }
        foodImage.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        return view

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.e("TEST","onActivityResult: photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver,selectedPhotoUri)

            foodImage.setImageBitmap(bitmap)

        }
    }

    private fun getAndSetNewMenuMap (profileImageURL: String = "") {
        val truckImage = profileImageURL
        val itemName = editItemName.text.toString()
        val itemPrice = editItemPrice.text.toString()
        val map = mutableMapOf<String, Any>()
        if (itemName.isNotEmpty()) {
            map["foodName"] = itemName
        }
        if (itemPrice.isNotEmpty()) {
            try {
                map["foodPrice"] = itemPrice.toInt()
            } catch (e: Exception) {
                return
            }

        }
        if (truckImage.isNotEmpty()) {
            map["foodImage"] = truckImage
        }
        if(DataManager.menus.size < 10) {
            auth.currentUser?.let {
                db.collection("OwnerMenus").document(it.uid).collection("Items").add(map)
               /* Log.d("!!!", "getAndSetNewMenuMap: $activity")
                Toast.makeText(activity?.applicationContext, "Successfully added to your menu!", Toast.LENGTH_SHORT ).show()

                */
            }
        }
    }

    private fun uploadImageToFirebaseStorage() {

        if(selectedPhotoUri != null) {
            val filename = UUID.randomUUID().toString()
            val imagesRef = FirebaseStorage.getInstance().getReference("/images/$filename")

            imagesRef.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.e("TEST", "onUpload: successfully uploaded image: ${it.metadata?.path}")

                    imagesRef.downloadUrl.addOnSuccessListener {
                        it.toString()
                        Log.e("TEST", "onUpload: FileLocation: $it")

                        getAndSetNewMenuMap(it.toString())
                    }
                }
        }

    }

    private fun removeFragment () {
        parentFragmentManager.beginTransaction()?.remove(this)?.commit()
      //  activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }
}
