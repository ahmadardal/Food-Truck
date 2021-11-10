package com.example.food_trock.models

data class User(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: String = "",
    val favorites: List<String> = ArrayList(),
    val role : Roles = Roles(false,false,false)

) {
}
