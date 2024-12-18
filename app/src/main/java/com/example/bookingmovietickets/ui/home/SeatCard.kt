package com.example.bookingmovietickets.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookingmovietickets.data.model.phong.Seat

@Composable
fun SeatCard(
    seat: Seat,
    onClick: (Seat) -> Unit,
    isSelected: Boolean,
    isBooked: Boolean
) {
    Card(
        modifier = Modifier
            .size(32.dp) // Kích thước ghế
            .clickable(enabled = !isBooked) { onClick(seat) }, // vo hieu hoa ghe da dat
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor =  when {
                isBooked -> Color.Red // Ghế đã được đặt
                isSelected -> Color.Blue // Ghế đang được chọn
                else -> Color.LightGray // Ghế trống
            }
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = seat.type + seat.seatNumber,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = when {
                    isBooked -> Color.White // Ghế đã đặt hiển thị chữ màu trắng
                    isSelected -> Color.White
                    else -> Color.Black
                }
            )
        }
    }
}

