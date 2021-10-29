package com.example.food_trock.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.fragments.MenuListFragment
import com.example.food_trock.models.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class menuListAdapter (val context: Context, val menuList: List<MenuItem> ) :
RecyclerView.Adapter<menuListAdapter.menuViewHolder>(){

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    fun removeMenu(position: Int) {
        db = Firebase.firestore
        auth = Firebase.auth
        val storage = Firebase.storage

        DataManager.menus[position].foodImage?.let { storage.getReferenceFromUrl(it).delete() }

        DataManager.menus[position].documentID?.let {
            db.collection("OwnerMenus").document(auth.currentUser!!.uid)
                .collection("Items").document(it).delete()
            Toast.makeText(context,"Successfully deleted ${DataManager.menus[position].foodName}."
                , Toast.LENGTH_SHORT).show()
        }

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): menuViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false)
        return menuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentItem = menuList[position]
        holder.txtItemName.text = currentItem.foodName
        holder.txtItemPrice.text = currentItem.foodPrice.toString()
        holder.itemPosition = position
        Glide.with(context).load(currentItem.foodImage).into(holder.itemImage)

    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class menuViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val txtItemName: TextView = itemView.findViewById(R.id.txtItemName)
        val txtItemPrice: TextView = itemView.findViewById(R.id.txtItemPrice)
        val itemImage: ImageView = itemView.findViewById(R.id.menuItemImage)
        val removeMenuBtn = itemView.findViewById<ImageView>(R.id.removeMenuBtn)
        var itemPosition = 0


        init {
            removeMenuBtn.setOnClickListener() {
                Log.e("!!!","onRemove: Item position: $itemPosition MenuSize: ${DataManager.menus.size}")
                removeMenu(itemPosition)
            }
        }
    }




}