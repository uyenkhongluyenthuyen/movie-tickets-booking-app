package com.example.bookingmovietickets.data.model.taikhoan

data class Order(
    val id: String = "",
    val time: String = "",
    val status: Boolean = false,
    val totalAmount: Double = 0.0,
    val ticketIds: List<String> = emptyList()
)
