package com.example.food_trock.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.adapters.StoreMenuListAdapter
import com.example.food_trock.adapters.menuListAdapter
import com.example.food_trock.models.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StoreFragment : Fragment() {

    lateinit var txtStoreName: TextView
    lateinit var txtPriceClass: TextView
    lateinit var txtDistance: TextView
    lateinit var storeImage: ImageView
    lateinit var recyclerView: RecyclerView
    val storeMenuList = mutableListOf<MenuItem>()
    lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.store_fragment, container, false,)

        recyclerView = view.findViewById(R.id.recyclerView3)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        var StoreMenuAdapter = StoreMenuListAdapter(view.context, storeMenuList)
        recyclerView.adapter = StoreMenuAdapter

        val returnBtn = view.findViewById<ImageButton>(R.id.storeReturnBtn)
        txtStoreName = view.findViewById(R.id.txtStoreName)
        txtPriceClass = view.findViewById(R.id.txtPriceClass)
        txtDistance = view.findViewById(R.id.txtDistance)
        storeImage = view.findViewById(R.id.imagewView2)
        db = Firebase.firestore

        var storeName: String? = arguments?.getString("storeName")
        var storePriceClass: Int? = arguments?.getInt("storePriceClass")
        var storeDistance: String? = arguments?.getString("storeDistance")
        var storeIMG: String? = arguments?.getString("storeImage")
        var storeID: String? = arguments?.getString("storeID")

        txtStoreName.text = storeName
        txtPriceClass.text = storePriceClass.toString()
        txtDistance.text = storeDistance
        if (storeIMG != null) {
            Glide.with(this).load(storeIMG).into(storeImage)
        }



        storeMenuList.clear()
        if (storeID != null) {
            if (storeID.isNotEmpty()) {
                db.collection("OwnerMenus").document(storeID).collection("Items").get()
                    .addOnSuccessListener { snapshot ->
                        if(snapshot != null) {
                            for(menu in snapshot.documents) {
                                val item = menu.toObject(MenuItem::class.java)
                                if (item != null) {
                                    storeMenuList.add(item)
                                }
                            }
                        }
                        recyclerView.adapter?.notifyDataSetChanged()
                    }


                if (storePriceClass != null) {
                    if (storePriceClass <= 70) {
                        txtPriceClass.text = "$"
                    } else if (storePriceClass in 71..110) {
                        txtPriceClass.text = "$$"
                    } else
                        txtPriceClass.text = "$$$"
                }
            }
        }

        returnBtn.setOnClickListener() {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        return view


    }

}