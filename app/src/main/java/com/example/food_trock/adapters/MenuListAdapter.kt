package com.example.food_trock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.food_trock.models.Menu
import com.example.food_trock.R

class menuListAdapter (val context: Context, val menuList: List<Menu> ) :
RecyclerView.Adapter<menuListAdapter.menuViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): menuViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false)
        return menuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        val currentItem = menuList[position]
        holder.itemIMG.setImageResource(currentItem.menuIMG)
        holder.txtItemName.text = currentItem.menuName
        holder.txtItemPrice.text = currentItem.menuPrice.toString()

    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class menuViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val itemIMG: ImageView = itemView.findViewById(R.id.ItemIMG)
        val txtItemName: TextView = itemView.findViewById(R.id.txtItemName)
        val txtItemPrice: TextView = itemView.findViewById(R.id.txtItemPrice)
    }


}