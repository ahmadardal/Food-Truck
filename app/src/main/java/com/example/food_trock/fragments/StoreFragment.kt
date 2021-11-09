package com.example.food_trock.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.adapters.StoreMenuListAdapter
import com.example.food_trock.adapters.menuListAdapter
import com.example.food_trock.models.MenuItem
import com.example.food_trock.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.store_fragment.*
import kotlinx.android.synthetic.main.store_item.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class StoreFragment : Fragment() {

    lateinit var txtStoreName: TextView
    lateinit var txtPriceClass: TextView
    lateinit var txtDistance: TextView
    lateinit var txtPhoneNumber: TextView
    lateinit var txtOpeningHours: TextView
    lateinit var txtStoreRating: TextView
    lateinit var storeImage: ImageView
    lateinit var recyclerView: RecyclerView
    val storeMenuList = mutableListOf<MenuItem>()
    var db: FirebaseFirestore = Firebase.firestore
    var auth: FirebaseAuth = Firebase.auth
    val usersReference = auth.currentUser?.let { db.collection("Users").document(it.uid) }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.store_fragment, container, false)

        recyclerView = view.findViewById(R.id.recyclerView3)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        var StoreMenuAdapter = StoreMenuListAdapter(view.context, storeMenuList)
        recyclerView.adapter = StoreMenuAdapter

        val returnBtn = view.findViewById<ImageButton>(R.id.storeReturnBtn)
        val favBtn = view.findViewById<ImageButton>(R.id.favBtn)
        txtStoreName = view.findViewById(R.id.txtStoreName)
        txtPriceClass = view.findViewById(R.id.txtPriceClass)
        txtDistance = view.findViewById(R.id.txtDistance)
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber)
        txtOpeningHours = view.findViewById(R.id.txtOpeningHours)
        txtStoreRating = view.findViewById(R.id.txtStoreRating)
        storeImage = view.findViewById(R.id.imagewView2)


        setStoreInfo()
        setFavorite()
        refreshList()



        returnBtn.setOnClickListener() {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        favBtn.setOnClickListener() {
            isFavorite()
        }

        return view


    }

    fun refreshList () {

        var storeID: String? = arguments?.getString("storeID")
        storeMenuList.clear()
        if (storeID != null) {
            if (storeID!!.isNotEmpty()) {
                db.collection("OwnerMenus").document(storeID!!).collection("Items").get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot != null) {
                            for (menu in snapshot.documents) {
                                val item = menu.toObject(MenuItem::class.java)
                                if (item != null) {
                                    storeMenuList.add(item)
                                }
                            }
                        }
                        recyclerView.adapter?.notifyDataSetChanged()
                    }

            }
        }
    }

    fun addFavorite(isFavorite: Boolean) {


        var storeID: String? = arguments?.getString("storeID")

        if (isFavorite) {
            usersReference?.update("favorites", FieldValue.arrayRemove(storeID))

            context?.let { ContextCompat.getColor(it, R.color.white) }?.let {
                favBtn.setColorFilter(
                    it, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            cardViewFavorite.setCardBackgroundColor(Color.parseColor("#EF3D64"))

        } else if (!isFavorite) {

            usersReference?.update("favorites", FieldValue.arrayUnion(storeID))

            context?.let { ContextCompat.getColor(it, R.color.red) }?.let {
                favBtn.setColorFilter(
                    it, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            cardViewFavorite.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        }

    }

    private fun isFavorite(){

        val storeID: String? = arguments?.getString("storeID")


        usersReference?.get()?.addOnSuccessListener { task ->
            Log.e("!!!","onSuccess")
            if(task != null) {
                Log.e("!!!","Task is not null")
                val favorite = task.get("favorites") as List <*>
                if(favorite.isEmpty()) {
                    addFavorite(false)
                }
                for (i in favorite) {
                    if (i.toString() == storeID) {

                        context?.let { ContextCompat.getColor(it, R.color.red) }?.let {
                            favBtn.setColorFilter(
                                it, android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                        cardViewFavorite.setCardBackgroundColor(Color.parseColor("#FFFFFF"))

                        addFavorite(true)
                        Log.e("!!!","true")
                        break
                    } else {
                        addFavorite(false)
                        Log.e("!!!","false")
                    }
                }
            }
        }

    }

    fun setFavorite () {
        val storeID: String? = arguments?.getString("storeID")

        usersReference?.get()?.addOnSuccessListener { task ->
            if(task != null) {
                val favorite = task.get("favorites") as List <*>
                for (i in favorite) {
                    if (i.toString() == storeID) {

                        context?.let { ContextCompat.getColor(it, R.color.red) }?.let {
                            favBtn.setColorFilter(
                                it, android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                        cardViewFavorite.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                }
            }
        }


    }

    fun setStoreInfo() {

        val storeName: String? = arguments?.getString("storeName")
        val storePriceClass: Int? = arguments?.getInt("storePriceClass")
        val storeDistance: String? = arguments?.getString("storeDistance")
        val storeIMG: String? = arguments?.getString("storeImage")
        var storeID: String? = arguments?.getString("storeID")
        val openHrs: String? = arguments?.getString("openHrs")
        val phoneNumber: String? = arguments?.getString("phoneNumber")
        val storeRating: Float? = arguments?.getFloat("rating")

        if (storePriceClass != null) {
            if (storePriceClass!! <= 70) {
                txtPriceClass.text = "$"
            } else if (storePriceClass in 71..105) {
                txtPriceClass.text = "$$"
            } else
                txtPriceClass.text = "$$$"
        }
        txtPriceClass.setTextColor(Color.parseColor("#A61830"))
        txtStoreName.text = storeName
        txtDistance.text = storeDistance
        txtOpeningHours.text = openHrs
        txtPhoneNumber.text = phoneNumber
        txtStoreRating.text = storeRating.toString()

        if (storeIMG != null) {
            Glide.with(this).load(storeIMG).into(storeImage)
        }
    }


}