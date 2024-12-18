package com.example.bookingmovietickets.data.model.taikhoan

data class Account(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val orders: List<Order> = emptyList()
)
