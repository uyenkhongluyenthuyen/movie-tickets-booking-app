@file:Suppress("NAME_SHADOWING")

package com.example.bookingmovietickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookingmovietickets.ui.home.BillScreen
import com.example.bookingmovietickets.ui.home.BookingTicketScreen
import com.example.bookingmovietickets.ui.home.HomeScreen
import com.example.bookingmovietickets.ui.home.MovieDetailScreen
import com.example.bookingmovietickets.ui.home.MovieReviewScreen
import com.example.bookingmovietickets.ui.home.MovieScreen
import com.example.bookingmovietickets.ui.navigation.NavRoute
import com.example.bookingmovietickets.ui.theme.BookingMovieTicketsTheme
import com.example.bookingmovietickets.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(35)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookingMovieTicketsTheme {
                val movieViewModel: MovieViewModel = hiltViewModel()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                NavHost(
                    navController = navController,
                    startDestination = NavRoute.HomeScreen.route,
                ) {
                    addHomeScreen(navController, this)
                    addMovieReviewScreen(navController, this, movieViewModel)
                    addMovieScreen(navController, this, movieViewModel)
                    addMovieDetailScreen(navController, this, movieViewModel)
                    addBookingTicketScreen(navController, this, movieViewModel)
                    addBillScreen(navController, this)
                }
            }
        }
    }
}

@RequiresApi(35)
fun addHomeScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable(
        route = NavRoute.HomeScreen.route
    ) {
        HomeScreen(
            onNavigationToReview = {
                navController.navigate(NavRoute.MovieReviewScreen.route)
            },
            onNavigationToMovie = {
                navController.navigate(NavRoute.MovieScreen.route)
            }
        )

    }
}

fun addMovieReviewScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder,
    movieViewModel: MovieViewModel
) {
    navGraphBuilder.composable(
        route = NavRoute.MovieReviewScreen.route
    ) {
        MovieReviewScreen(movieViewModel){
            navController.navigate(NavRoute.HomeScreen.route)
        }
    }
}

fun addMovieScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder,
    movieViewModel: MovieViewModel
) {
    navGraphBuilder.composable(
        route = NavRoute.MovieScreen.route
    ) {
        MovieScreen(movieViewModel) { movieId ->
            navController.navigate(NavRoute.MovieDetailScreen.route + "/$movieId")
        }
    }
}

fun addMovieDetailScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder,
    movieViewModel: MovieViewModel
) {
    navGraphBuilder.composable(
        route = NavRoute.MovieDetailScreen.route + "/{movieId}",
        arguments = listOf(
            navArgument(name = "movieId") {
                type = NavType.StringType
            }
        )
    ) { navBackStackEntry ->
        navBackStackEntry.arguments?.let {
            val movieId = it.getString("movieId")
            if (movieId != null) {
                MovieDetailScreen(
                    movieId = movieId,
                    onNavigationToBookingTicket = { movieId ->
                        navController.navigate(NavRoute.BookingTicketScreen.route + "/$movieId")
                    },
                    movieViewModel
                )
            }
        }
    }
}

@RequiresApi(35)
fun addBookingTicketScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder,
    movieViewModel: MovieViewModel
) {
    navGraphBuilder.composable(
        route = NavRoute.BookingTicketScreen.route + "/{movieId}",
        arguments = listOf(
            navArgument(name = "movieId") {
                type = NavType.StringType
            }
        )) { navBackStackEntry ->
        navBackStackEntry.arguments?.let {
            val movieId = it.getString("movieId")
            if (movieId != null)
                BookingTicketScreen(
                    movieId = movieId,
                    movieViewModel = movieViewModel
                ){
                    navController.navigate(NavRoute.BillScreen.route)
                }
        }
    }
}


fun addBillScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder){
    navGraphBuilder.composable(
        route = NavRoute.BillScreen.route
    ){
        BillScreen(){
            navController.navigate(NavRoute.HomeScreen.route)
        }
    }
}