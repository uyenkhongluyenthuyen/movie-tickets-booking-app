package com.example.bookingmovietickets.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookingmovietickets.R
import com.example.bookingmovietickets.data.model.phim.Movie
import com.example.bookingmovietickets.data.model.phim.MovieType
import com.example.bookingmovietickets.data.model.phim.Review
import com.example.bookingmovietickets.viewmodel.MovieViewModel

@Composable
fun MovieScreen(movieViewModel: MovieViewModel, onMovieDetailClick: (String) -> Unit) {
    LaunchedEffect(Unit) {
        movieViewModel.fetchMovies()
    }
    LaunchedEffect(true) {
        movieViewModel.resetData()
    }
    val movies by movieViewModel.movies.collectAsState()

    Column(
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)

    ) {
        Text(
            modifier = Modifier.padding(start = 14.dp),
            text = "Movies",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyVerticalGrid(
            modifier = Modifier
                .padding(start = 14.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movies) { movie ->
                MovieItemScreen(movie = movie, onMovieDetailClick = {movieId ->
                    onMovieDetailClick(movieId)
                })
            }
        }
    }

}

@Composable
fun MovieItemScreen(movie: Movie, onMovieDetailClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                onMovieDetailClick(movie.id)
            }
    ) {
        Card(
            modifier = Modifier
                .padding(4.dp)
                .width(150.dp)
                .height(220.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            val img = when (movie.id) {
                "movie_1" -> R.drawable.movie_1
                "movie_2" -> R.drawable.movie_2
                "movie_3" -> R.drawable.movie_3
                "movie_4" -> R.drawable.movie_4
                "movie_5" -> R.drawable.movie_5
                else -> R.drawable.image_default // Hình ảnh mặc định
            }
            Image(
                modifier = Modifier
                    .width(150.dp)
                    .height(220.dp),
                painter = painterResource(id = img),
                contentDescription = null )
        }
        Text(
            text = movie.name,
            fontSize = 12.sp,
            fontWeight = FontWeight(400),
            modifier = Modifier.padding(start = 4.dp)
        )
        Row(
            modifier = Modifier.padding(start = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for (i in 1..5) {
                Image(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp)
                        .padding(1.dp),
                    painter = painterResource(id = R.drawable.ic_star_rate_24),
                    contentDescription = "star rate",
                    colorFilter = ColorFilter.tint(colorResource(id = R.color.yellow))
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun MovieScreenPreview() {
    MovieItemScreen(movie = movie, {})

}

val movie = Movie(
    id = "movie_1",
    name = "Endgame Honeymoon",
    information = "Hành trình cuối cùng của các Avengers để cứu vũ trụ.",
    movieTypes = listOf(
        MovieType(
            id = "type_1",
            name = "Hành động"
        ),
        MovieType(
            id = "type_2",
        )
    ),
    time = 181,
    image = "https://example.com/avengers.jpg",
    reviews = listOf(
        Review(
            id = "m1_review_1",
            accountId = "account_1",
            content = "Phim rất hay, cảm động!",
            rate = 5
        ),
        Review(
            id = "m1_review_2",
            accountId = "account_2",
            content = "Thích đoạn chiến đấu cuối cùng!",
            rate = 4
        )
    )
)

