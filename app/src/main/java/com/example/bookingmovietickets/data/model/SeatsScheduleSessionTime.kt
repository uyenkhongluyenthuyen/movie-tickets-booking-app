package com.example.bookingmovietickets.data.model

import com.example.bookingmovietickets.data.model.phong.Seat

data class SeatsScheduleSessionTime(
    val id: String = "",
    val sessionTimeId: String = "",
    val scheduleId: String = "",
    val seats: List<Seat> = emptyList()
)