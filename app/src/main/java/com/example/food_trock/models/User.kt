package com.example.food_trock.models

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val favorites: List<Favorite> = ArrayList(),
    val role : Roles

) {
}
