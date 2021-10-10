package com.example.food_trock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class menuListAdapter (val context: Context, val menuList: List<Menu> ) :
RecyclerView.Adapter<menuListAdapter.menuViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): menuListAdapter.menuViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false)
        return menuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: menuListAdapter.menuViewHolder, position: Int) {
        val currentItem = menuList[position]
        holder.itemIMG.setImageResource(currentItem.menuIMG)
        holder.txtItemName.text = currentItem.menuName
        holder.txtItemInfo.text = currentItem.menuDescription
        holder.txtItemPrice.text = currentItem.menuPrice.toString()

    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class menuViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val itemIMG: ImageView = itemView.findViewById(R.id.ItemIMG)
        val txtItemName: TextView = itemView.findViewById(R.id.txtItemName)
        val txtItemInfo: TextView = itemView.findViewById(R.id.txtDescription)
        val txtItemPrice: TextView = itemView.findViewById(R.id.txtItemPrice)
    }


}