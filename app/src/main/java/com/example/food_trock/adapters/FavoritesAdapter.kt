package com.example.food_trock.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.store_fragment.*
import java.math.RoundingMode
import java.text.DecimalFormat

class FavoritesAdapter(val context: Context, val favoriteList: List<Store> ) :
    RecyclerView.Adapter<FavoritesAdapter.favoriteViewHolder>() {

    private lateinit var mListener : onItemClickListener
    val db: FirebaseFirestore = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth
    val usersReference = auth.currentUser?.let { db.collection("Users").document(it.uid) }


    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {

        mListener = listener

    }



    fun removeFavorite(position: Int) {

        usersReference?.update("favorites", FieldValue.arrayRemove(DataManager.favorites[position].UID))
        DataManager.favorites.removeAt(position)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favoriteViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false)
        return favoriteViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: favoriteViewHolder, position: Int) {
        val currentItem = favoriteList[position]
        holder.itemPosition = position
        Glide.with(context).load(currentItem.storeImage).into(holder.storeImage)
        holder.txtName.text = currentItem.storeName
        holder.txtPriceClass.text = currentItem.storePriceClass.toString()

        holder.txtDistance.text = distanceBetweenInKm(
            DataManager.currentLat,
            DataManager.currentLng, currentItem.storeLatitude, currentItem.storeLongitude)

        if(currentItem.category2 == "Empty" && currentItem.category1 != "Empty") {
            holder.txtCategory.text = "${currentItem.category1}"
        } else if (currentItem.category1 == "Empty" && currentItem.category2 != "Empty") {
            holder.txtCategory.text = "${currentItem.category2}"
        } else if (currentItem.category1 == "Empty" && currentItem.category2 == "Empty") {
            holder.txtCategory.text = " "
        } else {
            holder.txtCategory.text = "${currentItem.category1} | ${currentItem.category2}"
        }


        if(currentItem.storeStatus) {
            holder.txtStoreStatus.text = "ONLINE"
            holder.txtStoreStatus.setTextColor(Color.parseColor("#FF5EC538"))
        } else {
            holder.txtStoreStatus.text = "OFFLINE"
            holder.txtStoreStatus.setTextColor(Color.parseColor("#837E7E"))
        }


        context?.let { ContextCompat.getColor(it, R.color.red) }?.let {
            holder.favBtn.setColorFilter(
                it, android.graphics.PorterDuff.Mode.SRC_IN
            )
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

    fun distanceBetweenInKm (startLatitude: String,
                             startLongitude: String,
                             endLatitude: Double,
                             endLongitude: Double,
    ):String {
        try {
            val startLat = startLatitude.toDouble()
            val startLong = startLongitude.toDouble()
            val endLat = endLatitude
            val endLong = endLongitude

            val theta = startLong - endLong
            var dist = Math.sin(deg2rad(startLat)) * Math.sin(deg2rad(endLat)) + Math.cos(deg2rad(startLat)) * Math.cos(deg2rad(endLat)) * Math.cos(deg2rad(theta))
            dist = Math.acos(dist)
            dist = rad2deg(dist)
            dist = dist * 60 * 1.1515
            dist = dist * 1.609344

            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING


            return (df.format(dist)+" km")
        }
        catch (e: Exception) {
            return "n/a";
        }




    }
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    inner class favoriteViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        var txtCategory: TextView = itemView.findViewById(R.id.txtCategory)
        val storeImage: ImageView = itemView.findViewById(R.id.storeImage)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtPriceClass: TextView = itemView.findViewById(R.id.txtPriceClass)
        val txtStoreStatus: TextView = itemView.findViewById(R.id.txtOnline)
        val favBtn: ImageButton = itemView.findViewById(R.id.favBtn)
        var itemPosition = 0
        val txtDistance: TextView = itemView.findViewById(R.id.txtDistance)
        // val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

            favBtn.setOnClickListener() {
                removeFavorite(itemPosition)
            }
        }

    }


}

