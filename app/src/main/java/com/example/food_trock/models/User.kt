package com.example.food_trock.models

data class User(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val favorites: List<String> = ArrayList(),
    val role : Roles = Roles(),

) {
}
