package com.example.bookingmovietickets.ui.home

import android.annotation.SuppressLint
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookingmovietickets.viewmodel.MovieViewModel

@RequiresApi(35)
@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(
    onNavigationToReview: () -> Unit,
    onNavigationToMovie: () -> Unit
) {
//    val movieViewModel: MovieViewModel = hiltViewModel()
//    movieViewModel.addMovieDataToFireStore()
//    movieViewModel.addRoomsDataToFireStore()
//      movieViewModel.addScheduleDataToFireStore()
//    movieViewModel.addSeatsScheduleSessionTime()
//    movieViewModel.addAccountDataToFireStore()
//    movieViewModel.generateTicket()
//    movieViewModel.updateSeatsScheduleSessionTime()
//    movieViewModel.updateSessionTime()
//    movieViewModel.updateSchedule()
//    movieViewModel.updateRoom()
//    movieViewModel.updateMovies()
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { onNavigationToReview() }) {
            Text(text = "Review")
        }
        Button(onClick = { onNavigationToMovie() }) {
            Text(text = "Booking ticket")
        }

    }
}