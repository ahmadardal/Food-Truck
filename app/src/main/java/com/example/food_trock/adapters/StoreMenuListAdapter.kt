package com.example.food_trock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_trock.R
import com.example.food_trock.models.MenuItem


class StoreMenuListAdapter (val context: Context, val storeMenuList: List<MenuItem> ) :
    RecyclerView.Adapter<StoreMenuListAdapter.menuViewHolder>(){



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): menuViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.menu_store_item, parent, false)
        return menuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentItem = storeMenuList[position]
        holder.txtItemName.text = currentItem.foodName
        holder.txtItemPrice.text = "${currentItem.foodPrice} :-"
        Glide.with(context).load(currentItem.foodImage).into(holder.foodIMG)

    }

    override fun getItemCount(): Int {
        return storeMenuList.size
    }

    inner class menuViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val txtItemName: TextView = itemView.findViewById(R.id.txtFoodName)
        val txtItemPrice: TextView = itemView.findViewById(R.id.txtFoodPrice)
        val foodIMG: ImageView = itemView.findViewById(R.id.foodImg)


    }




}