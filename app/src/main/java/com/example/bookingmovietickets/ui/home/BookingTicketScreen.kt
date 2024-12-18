package com.example.bookingmovietickets.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookingmovietickets.R
import com.example.bookingmovietickets.utils.convertDateFormat
import com.example.bookingmovietickets.data.model.lichchieu.SessionTime
import com.example.bookingmovietickets.data.model.phong.Seat
import com.example.bookingmovietickets.data.model.taikhoan.Order
import com.example.bookingmovietickets.utils.generateSeatsToTickets
import com.example.bookingmovietickets.utils.generateTicketsToTicketIds
import com.example.bookingmovietickets.utils.getCurrentDate
import com.example.bookingmovietickets.utils.getCurrentTime
import com.example.bookingmovietickets.viewmodel.MovieViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.log

@RequiresApi(35)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BookingTicketScreen(
    movieId: String,
    movieViewModel: MovieViewModel,
    onNavigationToBill: () -> Unit
) {
    val accountId = "account_5"

    var selectedDate by remember { mutableStateOf<String>("") }
    var selectedSessionTime by remember { mutableStateOf<SessionTime?>(null) }
    var selectedSeats by remember { mutableStateOf<List<Seat>>(emptyList()) }

    val sessionTimes = movieViewModel.sessionTimes.collectAsState()
    val loading = movieViewModel.loading.collectAsState()

    val seatsScheduleSessionTime = movieViewModel.seatsScheduleSessionTime.collectAsState()
    val loadingSeats = movieViewModel.loadingSeats.collectAsState()

    val updateOderState = movieViewModel.updateOrderState.collectAsState()


    Column(
        modifier = Modifier
            .padding(top = 32.dp, bottom = 36.dp)
            .fillMaxSize()
    ) {
        //day of the week and date
        BookingDatePicker(onDateSelected = { date ->
            selectedDate = date
        })
        Spacer(modifier = Modifier.height(16.dp))

        //session time
        BookingSessionTime(
            sessionTimes = sessionTimes.value,
            loading = loading.value,
            selectedSessionTime = selectedSessionTime,
            onSessionTimeSelected = { sessionTime ->
                if (sessionTime == selectedSessionTime) {
                    selectedSessionTime = null
                } else {
                    selectedSessionTime = sessionTime
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        //list seat of schedule session
        BookingSeat(
            loadingSeat = loadingSeats.value,
            listSeat = seatsScheduleSessionTime.value.seats,
            selectedSeats = selectedSeats,
            onSeatSelected = { seat ->
                selectedSeats = if (selectedSeats.contains(seat)) {
                    selectedSeats - seat // Remove if already selected
                } else {
                    selectedSeats + seat // Add if not selected
                }
            }
        )
        // Spacer sẽ chiếm hết không gian còn lại
        Spacer(modifier = Modifier.weight(1f))

        selectedSessionTime?.let {
            TotalTicketsPrice(
                selectedSeats = selectedSeats,
                price = it.price,
                onPayment = {
                    //tickets
                    val tickets =
                        generateSeatsToTickets(selectedSeats, seatsScheduleSessionTime.value.id)
                    Log.d("Tickets", tickets.toString())
                    //order
                    val order = Order(
                        id = "${accountId}_${getCurrentTime()}",
                        time = getCurrentDate(),
                        status = true,
                        totalAmount = selectedSeats.size * it.price,
                        ticketIds = generateTicketsToTicketIds(tickets)
                    )
                    Log.d("Order", order.toString())
                    // Handle payment logic here
                    movieViewModel.buyTicket(accountId, order, tickets)
                    Log.d("State", "${updateOderState.value}")
                    onNavigationToBill()
                }
            )
        } ?: run {
            // Handle case when no session is selected, maybe show a placeholder or warning

        }

    }
    // Fetch session times when movieId or selectedDate changes
    LaunchedEffect(selectedDate) {
        if (selectedDate != "") {
            movieViewModel.fetchSessionTimesByMovieAndDay(movieId, selectedDate)
            selectedSeats = emptyList() // Reset selected seats when date changes
            selectedSessionTime = null //reset selected session time when date changes
            movieViewModel.resetSeatsScheduleSessionTime() //reset seats when date changes
        }
    }

    LaunchedEffect(selectedSessionTime) {
        selectedSessionTime?.let {
            movieViewModel.fetchSeatsScheduleSessionTime(
                it.time,
                movieId,
                convertDateFormat(selectedDate),
                it.id
            )
            selectedSeats = emptyList() // Reset selected seats when session time changes
        }
    }

}


/**
 * Booking date picker
 */
@Composable
fun BookingDatePicker(
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val dateList = (0 until 14).map { offset ->
        calendar.apply { add(Calendar.DATE, offset) }.time.let { date ->
            val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
            Pair(dayOfWeek, formattedDate)
        }.also { calendar.add(Calendar.DATE, -offset) } // Reset calendar
    }

    // State to keep track of the selected date
    var selectedDate by remember { mutableStateOf<String?>("") }


    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dateList) { (dayOfWeek, formattedDate) ->
            DateCard(
                dayOfWeek = dayOfWeek,
                formattedDate = formattedDate,
                isSelected = selectedDate == formattedDate,
                onClick = {
                    selectedDate = if (selectedDate == formattedDate) "" else formattedDate
                    onDateSelected(formattedDate ?: "")
                }
            )
        }
    }
}


/**
 * booking a session time in the chosen day
 */

@Composable
fun BookingSessionTime(
    sessionTimes: List<SessionTime>,
    loading: Boolean,
    selectedSessionTime: SessionTime?,
    onSessionTimeSelected: (SessionTime) -> Unit

) {
//    var selectedSessionTime by remember { mutableStateOf<String?>(null) }


    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // Hiển thị vòng quay khi loading
        }
    } else {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sessionTimes) { sessionTime ->
                SessionCard(
                    sessionTime = sessionTime.time,
                    isSelected = selectedSessionTime?.time == sessionTime.time,
                    onClick = {
//                        selectedSessionTime =
//                            if (selectedSessionTime == sessionTime.time) null else sessionTime.time

                        onSessionTimeSelected(sessionTime)
                    }
                )
            }
        }
    }

}


/**
 * booking a seat
 */
@Composable
fun BookingSeat(
    loadingSeat: Boolean,
    listSeat: List<Seat>,
    selectedSeats: List<Seat>,
    onSeatSelected: (Seat) -> Unit
) {
    if (loadingSeat) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // Show loading indicator when loading seats
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5), // 5 seats per row
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listSeat) { seat ->
                SeatCard(
                    seat = seat,
                    isBooked = !seat.status,
                    isSelected = selectedSeats.contains(seat),
                    onClick = {
                        onSeatSelected(seat)
                    }
                )
            }
        }
    }
}

/**
 * Total tickets price
 */
@Composable
fun TotalTicketsPrice(
    selectedSeats: List<Seat>,
    price: Double,
    onPayment: () -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = if (selectedSeats.isNotEmpty()) "${selectedSeats.size * price}đ" else "0.0đ",
                fontSize = 20.sp,
                fontWeight = FontWeight(500),
                color = colorResource(id = R.color.color_default),
                modifier = Modifier.align(Alignment.CenterEnd) // Đặt chữ ở bên phải
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (selectedSeats.isEmpty()) {
                    Toast.makeText(context, "Please select at least one seat", Toast.LENGTH_SHORT).show()
                  return@Button
                }
                onPayment()
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF95E0A),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(size = 8.dp)
        ) {
            Text(text = "BUY TICKET", fontSize = 16.sp)
        }
    }

}