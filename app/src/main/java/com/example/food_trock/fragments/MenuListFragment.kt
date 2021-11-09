package com.example.food_trock.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.adapters.menuListAdapter
import com.example.food_trock.models.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuListFragment: Fragment() {


    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_addmenu, container, false,)


        recyclerView = view.findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        var addMenuAdapter = menuListAdapter(view.context, DataManager.menus)
        recyclerView.adapter = addMenuAdapter

        val addMenuBtn = view.findViewById<ImageButton>(R.id.addMenuBtn)
        val backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        db = Firebase.firestore
        auth = Firebase.auth


        addMenuBtn.setOnClickListener() {
            val addFoodFragment = AddFoodFragment()

            val transaction = childFragmentManager.beginTransaction()
            transaction?.add(R.id.addFoodContainer, addFoodFragment, "add")
            transaction?.commit()
        }
        backBtn.setOnClickListener() {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }


        db.collection("OwnerMenus").document(auth.currentUser!!.uid)
            .collection("Items").addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    DataManager.menus.clear()
                    if (value != null) {
                        for (document in value.documents) {
                            val item = document.toObject(MenuItem::class.java)
                            if (item != null) {
                                DataManager.menus.add(item)
                            }
                        }
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                    setAveragePrice()
                }
            })


        return view
    }

    fun countAveragePrice() : Int {
        var itemAddedPrice = 0
        var itemAveragePrice = 0

        for(item in DataManager.menus) {
            itemAddedPrice += item.foodPrice
        }

        if(itemAddedPrice > DataManager.menus.size) {
            itemAveragePrice = itemAddedPrice / DataManager.menus.size
        }

        return itemAveragePrice
    }

    fun setAveragePrice () {
        val newPriceClass = countAveragePrice()
        Log.e("!!!", "$newPriceClass")
        db.collection("FoodTrucks").document(auth.currentUser!!.uid)
            .update(mapOf("storePriceClass" to newPriceClass))
    }

}


