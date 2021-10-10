package com.example.food_trock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class storeAdapter(val context: Context, val storeList: List<Store> ) :
    RecyclerView.Adapter<storeAdapter.storeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): storeViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.store_item, parent, false)
        return storeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: storeViewHolder, position: Int) {
        val currentItem = storeList[position]
        holder.storeImage.setImageResource(currentItem.storeImage)
        holder.txtName.text = currentItem.storeName
        holder.txtDescription.text = currentItem.storeDescription
        holder.txtPriceClass.text = currentItem.storePriceClass
        holder.txtDistance.text = currentItem.storeDistance
        holder.ratingBar.rating = currentItem.storeRating.toFloat()
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    class storeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var cardView: CardView = itemView.findViewById(R.id.cardView)
        val storeImage: ShapeableImageView = itemView.findViewById(R.id.storeImage)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
        val txtPriceClass: TextView = itemView.findViewById(R.id.txtPriceClass)
        val txtDistance: TextView = itemView.findViewById(R.id.txtDistance)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)

    }

}