package com.example.bookingmovietickets.data.model.lichchieu


data class SessionTime(
    val id: String = "",
    val time: String ="00:00",
    val price: Double = 0.0,
    val scheduleIds: List<String> = emptyList()
)