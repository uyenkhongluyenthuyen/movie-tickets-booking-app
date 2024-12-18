package com.example.bookingmovietickets.utils

import com.example.bookingmovietickets.data.model.phong.Seat
import com.example.bookingmovietickets.data.model.taikhoan.Ticket
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertDateFormat(inputDate: String): String {
    return try {
        // Định dạng ngày đầu vào
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        // Định dạng ngày đầu ra
        val outputFormat = SimpleDateFormat("ddMMyy", Locale.getDefault())

        // Parse chuỗi ngày đầu vào và format sang định dạng mới
        val date = inputFormat.parse(inputDate)
        outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        "" // Trả về chuỗi rỗng nếu có lỗi
    }
}

fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("ddMMyyyy'T'HH:mm", Locale.getDefault())
    val currentDate = Date()
    return dateFormat.format(currentDate)
}

fun getCurrentDate(): String{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val currentDate = Date()
    return dateFormat.format(currentDate)
}

fun generateFakeSeats(totalSeats: Int, roomId: String): List<Seat> {
    val seatList = mutableListOf<Seat>()
    val seatTypes = listOf("A", "B", "C", "D", "E") // Loại ghế

    for (i in 1..totalSeats) {
        seatList.add(
            Seat(
                id = "${roomId}_seat_${i}", // Ví dụ: room_1_seat_1
                seatNumber = i.toString(),
                type = seatTypes[(i - 1) % seatTypes.size], // Loại ghế luân phiên A, B, C
                status = true // Ghế mặc định là trống (true)
            )
        )
    }
    return seatList
}

fun generateSeatsToTickets(seats: List<Seat>, seatsScheduleSessionTimeId: String): List<Ticket> {
   val tickets = mutableListOf<Ticket>()
   for (seat in seats){
       val ticket = Ticket(
           id = "${seat.id}.${seatsScheduleSessionTimeId}",
           seatId = seat.id,
           seatsScheduleSessionTimeId = seatsScheduleSessionTimeId
       )
       tickets.add(ticket)
   }
    return tickets
}

fun generateTicketsToTicketIds(tickets: List<Ticket>): List<String> {
    val ticketIds = mutableListOf<String>()
    for (ticket in tickets){
        ticketIds.add(ticket.id)
    }
    return ticketIds
}