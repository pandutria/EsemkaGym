package com.example.esemkagym.data.model

data class User(
    val admin: Boolean,
    val email: String,
    val gender: String,
    val id: Int,
    val joinedMemberAt: Any,
    val membershipEnd: Any,
    val name: String,
    val password: String,
    val registerAt: String
)