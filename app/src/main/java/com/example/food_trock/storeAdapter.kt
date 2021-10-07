package com.example.food_trock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class storeAdapter(private val storeList : ArrayList<Store>) :
    RecyclerView.Adapter<storeAdapter.storeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): storeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return storeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: storeViewHolder, position: Int) {
        val currentItem = storeList[position]
        holder.titleImage.setImageResource(currentItem.titleImage)
        holder.tvHeading.text = currentItem.heading
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    class storeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val titleImage : ShapeableImageView = itemView.findViewById(R.id.title_image)
        val tvHeading : TextView = itemView.findViewById(R.id.tvHeading)


    }

}