package com.example.food_trock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_trock.R
import com.example.food_trock.models.MenuItem
import com.example.food_trock.models.Store

class AdminTruckListAdapter (val context: Context, val adminTruckList: List<Store> ) :
    RecyclerView.Adapter<AdminTruckListAdapter.adminTruckViewHolder>() {


    fun truckSwitch () {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adminTruckViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.admin_foodtruck_item, parent, false)
        return adminTruckViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: adminTruckViewHolder, position: Int) {
        val currentItem = adminTruckList[position]
        holder.txt_Truck.text = currentItem.storeName
        holder.txt_Email.text

    }

    override fun getItemCount(): Int {
        return adminTruckList.size
    }

    inner class adminTruckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_Truck = itemView.findViewById<TextView>(R.id.txt_Truck)
        val txt_Email = itemView.findViewById<TextView>(R.id.txt_Email)
        val switch_Truck = itemView.findViewById<Switch>(R.id.switch_Truck)


    }
}




