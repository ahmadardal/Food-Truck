package com.example.food_trock.models

data class FoodTruckProfile(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val descriptionKeyWord: String = "",
    val foodTruckImage: String = "",
    val phoneNr: Long = 0,
    val country: String = "",
    val available: Boolean = false,
    val geoQueries: String = "",
    val openHours: String = "",
    val menu: List<Dishes> = ArrayList()
) {
}