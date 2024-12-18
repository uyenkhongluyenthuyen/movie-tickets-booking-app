package com.example.bookingmovietickets.data.model.phim

data class Movie(
    val id: String = "",
    val name: String = "",
    val information: String = "",
    val movieTypes: List<MovieType> = emptyList(),
    val time: Int = 0,
    val image: String = "",
    val reviews: List<Review> = emptyList()
)



