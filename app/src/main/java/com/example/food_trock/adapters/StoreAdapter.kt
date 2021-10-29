package com.example.food_trock.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class storeAdapter(val context: Context, val storeList: List<Store> ) :
    RecyclerView.Adapter<storeAdapter.storeViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {

        mListener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): storeViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.store_item, parent, false)
        return storeViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: storeViewHolder, position: Int) {
        val currentItem = storeList[position]
        Glide.with(context).load(currentItem.storeImage).into(holder.storeImage)
        holder.txtName.text = currentItem.storeName
        holder.txtPriceClass.text = currentItem.storePriceClass.toString()

        if(currentItem.category2 == "") {
            holder.txtCategory.text = "${currentItem.category1}"
        } else {
            holder.txtCategory.text = "${currentItem.category1} | ${currentItem.category2}"
        }



       // holder.txtDistance.text = currentItem.storeDistance
       // holder.ratingBar.rating = currentItem.storeRating.toFloat()

        if (currentItem.storePriceClass <= 70) {
            holder.txtPriceClass.text = "$"
        } else if (currentItem.storePriceClass in 71..105) {
            holder.txtPriceClass.text = "$$"
        } else
            holder.txtPriceClass.text = "$$$"


    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    inner class storeViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        var txtCategory: TextView = itemView.findViewById(R.id.txtCategory)
        val storeImage: ImageView = itemView.findViewById(R.id.storeImage)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtPriceClass: TextView = itemView.findViewById(R.id.txtPriceClass)
        val txtDistance: TextView = itemView.findViewById(R.id.txtDistance)
       // val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }


}

