package com.example.bookingmovietickets.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookingmovietickets.R
import com.example.bookingmovietickets.viewmodel.MovieViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MovieDetailScreen(movieId: String, onNavigationToBookingTicket: (String) -> Unit, movieViewModel: MovieViewModel) {
    LaunchedEffect(movieId) {
        movieViewModel.fetchMovieById(movieId)
    }
    val movie = movieViewModel.movieSelected.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Thêm hình ảnh nền
        val img = when (movieId) {
            "movie_1" -> R.drawable.movie_1
            "movie_2" -> R.drawable.movie_2
            "movie_3" -> R.drawable.movie_3
            "movie_4" -> R.drawable.movie_4
            "movie_5" -> R.drawable.movie_5
            else -> R.drawable.image_default
        }
        Image(
            painter = painterResource(id = img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Nội dung của MovieDetailScreen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp) // Thêm khoảng cách giữa các nội dung

            ) {
                // Các thành phần giao diện của bạn
                Text(
                    text = movie.value.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight(500),
                    color = Color.White
                )
                Text(
                    text = movie.value.information,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color.White
                )
                Button(
                    onClick = {
                        onNavigationToBookingTicket(movieId)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF95E0A),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(size = 8.dp)
                ) {
                    Text(text = "Booking Ticket")
                }
            }
        }

    }
}

