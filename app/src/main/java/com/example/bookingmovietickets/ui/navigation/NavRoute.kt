package com.example.bookingmovietickets.ui.navigation

sealed class NavRoute(val route: String) {
    object HomeScreen : NavRoute("home_screen")
    object MovieReviewScreen : NavRoute("movie_review_screen")
    object MovieScreen : NavRoute("movie_screen")
    object MovieDetailScreen : NavRoute("movie_detail_screen")
    object BookingTicketScreen : NavRoute("booking_ticket_screen")
    object BillScreen : NavRoute("bill_screen")
}