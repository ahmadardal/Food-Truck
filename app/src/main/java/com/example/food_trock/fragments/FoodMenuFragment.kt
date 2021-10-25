package com.example.food_trock.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
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

class FoodMenuFragment: Fragment() {

    lateinit var editItemName: EditText
    lateinit var editItemPrice: EditText
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

        editItemName = view.findViewById(R.id.editItemName)
        editItemPrice = view.findViewById(R.id.editItemPrice)
        val addMenuBtn = view.findViewById<ImageButton>(R.id.addMenuBtn)
        val backBtn = view.findViewById<ImageButton>(R.id.backBtn)
        db = Firebase.firestore
        auth = Firebase.auth


        addMenuBtn.setOnClickListener() {
            getAndSetNewMenuMap()
        }
        backBtn.setOnClickListener() {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }


            db.collection("OwnerMenus").document(auth.currentUser!!.uid)
                .collection("Items").addSnapshotListener(object: EventListener<QuerySnapshot> {
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
                    }
                })


        return view
    }

    private fun getAndSetNewMenuMap () {
        val itemName = editItemName.text.toString()
        val itemPrice = editItemPrice.text.toString()
        val map = mutableMapOf<String, Any>()
        if (itemName.isNotEmpty()) {
            map["foodName"] = itemName
        }
        if (itemPrice.isNotEmpty()) {
            map["foodPrice"] = itemPrice.toInt()
        }
        if(DataManager.menus.size < 10) {
            auth.currentUser?.let {
                db.collection("OwnerMenus").document(it.uid).collection("Items").add(map)
                Toast.makeText(context,"Successfully added $itemName.",Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context,"The maximum menu size is currently 10!",Toast.LENGTH_SHORT).show()
        }
        } }


