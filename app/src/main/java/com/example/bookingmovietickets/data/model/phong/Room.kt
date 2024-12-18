package com.example.bookingmovietickets.data.model.phong

    data class Room(
        val id: String = "",
        val totalSeat: Int = 0,
        val roomType: RoomType = RoomType(),
        val seats: List<Seat> = emptyList()
    )
