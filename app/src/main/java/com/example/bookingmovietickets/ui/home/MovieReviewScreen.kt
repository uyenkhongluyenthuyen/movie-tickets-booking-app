@file:Suppress("NAME_SHADOWING")

package com.example.bookingmovietickets.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookingmovietickets.R
import com.example.bookingmovietickets.data.model.phim.Movie
import com.example.bookingmovietickets.data.model.phim.Review
import com.example.bookingmovietickets.utils.getCurrentTime
import com.example.bookingmovietickets.viewmodel.MovieViewModel
import java.time.format.TextStyle

@Composable
fun MovieReviewScreen(movieViewModel: MovieViewModel, onNavigationToHome: () -> Unit) {
    val movieId = "movie_1"
    val accountId = "account_5"
    val context = LocalContext.current
    val movie = movieViewModel.movieSelected.collectAsState()

    var review by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(5) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, bottom = 32.dp, start = 16.dp, end = 16.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .width(200.dp)
                    .height(270.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(5.dp)
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
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = movie.value.name,
            fontSize = 24.sp,
            fontWeight = FontWeight(500),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = movie.value.information,
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        RatingStars {
            rating = it
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = review,
            onValueChange = {
                review = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Review")
            },
            maxLines = 5,
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if(review.isEmpty()){
                    Toast.makeText(context, "Please write your review", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                Log.d("rating", "${review} : ${rating}")
                val review = Review("${movieId}_${getCurrentTime()}",accountId, review, rating)
                movieViewModel.addReviewToFireStore(review, movieId)
                Toast.makeText(context, "Sent review successfully", Toast.LENGTH_SHORT).show()
                onNavigationToHome()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF95E0A),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(size = 8.dp)
        ) {
            Text(text = "Send review")
        }
    }

    LaunchedEffect(movieId) {
        movieViewModel.fetchMovieById(movieId)
    }

}

@Composable
fun RatingStars(onStarSelected: (Int) -> Unit) {
    var selectedRating by remember { mutableStateOf(0) } // Lưu trạng thái sao đã chọn

    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Hiển thị 5 sao
        for (i in 1..5) {
            val isSelected = i <= selectedRating
            Image(
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
                    .padding(4.dp)
                    .clickable {
                        selectedRating = i // Cập nhật sao đã chọn
                        onStarSelected(i) // Gọi callback trả về giá trị sao đã chọn
                    },
                painter = painterResource(id = if (isSelected) R.drawable.ic_star_rate_24 else R.drawable.ic_star_outline_24),
                contentDescription = "star rate",
                colorFilter = ColorFilter.tint(colorResource(id = R.color.yellow))
            )
        }
    }
}