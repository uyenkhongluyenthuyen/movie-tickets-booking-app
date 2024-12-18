package com.example.bookingmovietickets.data.repository

import android.util.Log
import androidx.annotation.RequiresApi
import com.example.bookingmovietickets.data.model.ResultResponse
import com.example.bookingmovietickets.data.model.SeatsScheduleSessionTime
import com.example.bookingmovietickets.data.model.lichchieu.Schedule
import com.example.bookingmovietickets.data.model.lichchieu.SessionTime
import com.example.bookingmovietickets.data.model.phim.Review
import com.example.bookingmovietickets.data.model.phim.Movie
import com.example.bookingmovietickets.data.model.phong.Room
import com.example.bookingmovietickets.data.model.phong.Seat
import com.example.bookingmovietickets.data.model.taikhoan.Account
import com.example.bookingmovietickets.data.model.taikhoan.Order
import com.example.bookingmovietickets.data.model.taikhoan.Ticket
import com.example.bookingmovietickets.utils.fakeAccountData
import com.example.bookingmovietickets.utils.fakeMoviesData
import com.example.bookingmovietickets.utils.fakeRoomsData
import com.example.bookingmovietickets.utils.fakeSchedules
import com.example.bookingmovietickets.utils.fakeSessionTimes
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class FirebaseRepository
@Inject constructor(@Named("FireStoreInstance") val firestore: FirebaseFirestore) {

    suspend fun getMovies(): ResultResponse<List<Movie>> {
        return try {
            val moviesCollectionRef = firestore.collection("movies")
            val querySnapshot = moviesCollectionRef.get().await()
            val movies = querySnapshot.documents.mapNotNull { document ->
                document.toObject(Movie::class.java)?.copy(id = document.id)
            }
            ResultResponse.Success(movies)
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }

    suspend fun getMovieById(movieId: String): ResultResponse<Movie> {
        return try {
            val movieDocumentRef = firestore.collection("movies").document(movieId)
            val documentSnapshot = movieDocumentRef.get().await()
            if (documentSnapshot.exists()) {
                // Chuyển đổi dữ liệu trong documentSnapshot thành Movie
                val movie = documentSnapshot.toObject(Movie::class.java)
                if (movie != null) {
                    ResultResponse.Success(movie)
                } else {
                    ResultResponse.Error(Exception("Movie not found"))
                }
            } else {
                ResultResponse.Error(Exception("Movie not found"))
            }
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }

    suspend fun fetchSchedulesByMovieAndDay(
        movieId: String,
        day: String
    ): ResultResponse<List<Schedule>> {
        return try {
            val schedulesCollectionRef = firestore.collection("schedules")
            val querySnapshot = schedulesCollectionRef
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("day", day)
                .get()
                .await()

            val schedules = querySnapshot.documents.mapNotNull { document ->
                document.toObject(Schedule::class.java)?.copy(id = document.id)
            }

            ResultResponse.Success(schedules)
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }

    suspend fun fetchSessionTimesByScheduleIds(scheduleIds: List<String>): ResultResponse<List<SessionTime>> {
        return try {
            val sessionTimesCollectionRef = firestore.collection("sessionTimes")
            val querySnapshot = sessionTimesCollectionRef
                .whereArrayContainsAny("scheduleIds", scheduleIds)
                .get()
                .await()

            val sessionTimes = querySnapshot.documents.mapNotNull { document ->
                document.toObject(SessionTime::class.java)?.copy(id = document.id)
            }

            ResultResponse.Success(sessionTimes)
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }

    suspend fun fetchScheduleIdBySessionTimeId(
        time: String,
        movieId: String,
        date: String
    ): ResultResponse<String> {
        return try {
            val sessionTimesCollectionRef = firestore.collection("sessionTimes")
            val querySnapshot = sessionTimesCollectionRef
                .whereEqualTo("time", time)
                .get()
                .await()
            Log.d("time", time)
            Log.d("MMM", querySnapshot.toString())
            if (!querySnapshot.isEmpty) {
                // Lấy bản ghi đầu tiên
                val document = querySnapshot.documents.first()
                Log.d("MMM", document.toString())

                // Lấy danh sách scheduleIds
                val scheduleIds = document.get("scheduleIds") as? List<String>
                Log.d("MMM2", scheduleIds.toString())


                // Kiểm tra và trả về scheduleId phù hợp
                val scheduleId = scheduleIds?.find { scheduleId ->
                    scheduleId.contains(movieId) && scheduleId.contains(date)
                }
                if (scheduleId != null) {
                    Log.d("MMM3", scheduleId)
                    ResultResponse.Success(scheduleId)
                } else {
                    ResultResponse.Error(Exception("No matching scheduleId found"))
                }

            } else {
                ResultResponse.Error(Exception("No matching session time found"))
            }
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }

    suspend fun fetchSeatsScheduleSessionTimes(scheduleId: String, sessionTimeId: String):
            ResultResponse<SeatsScheduleSessionTime> {
        return try {
            val seatsScheduleSessionTimesCollectionRef =
                firestore.collection("seatsScheduleSessionTimes")
            val querySnapshot = seatsScheduleSessionTimesCollectionRef
                .whereEqualTo("scheduleId", scheduleId)
                .whereEqualTo("sessionTimeId", sessionTimeId)
                .get()
                .await()

            val documentSnapshot = querySnapshot.documents.firstOrNull()
            if (documentSnapshot != null) {
                // Chuyển đổi tài liệu thành đối tượng SeatsScheduleSessionTime
                val seatsScheduleSessionTime = documentSnapshot
                    .toObject(SeatsScheduleSessionTime::class.java)?.copy(id = documentSnapshot.id)
                Log.d("seats", seatsScheduleSessionTime.toString())
                ResultResponse.Success(seatsScheduleSessionTime)
            } else {
                ResultResponse.Error(Exception("No matching seatsScheduleSessionTime found"))
            }
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }

    //update list orders
    @RequiresApi(35)
    suspend fun updateOrderInListOrders(accountId: String, order: Order): ResultResponse<String> {
        return try {
            val accountDocRef = firestore.collection("accounts").document(accountId)
            // get current document
            val snapshot = accountDocRef.get().await()
            if (snapshot.exists()) {
                // get current orders from Firestore
                val account = snapshot.toObject(Account::class.java)
                val currentOrders = account?.orders?.toMutableList() ?: mutableListOf()
                currentOrders.add(order)
                // update orders
                accountDocRef.update("orders", currentOrders).await()
            }
            ResultResponse.Success("Success")
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }

    }


    // update ticket in tickets
    suspend fun updateTickets(tickets: List<Ticket>) {
        try {
            val ticketsCollectionRef = firestore.collection("tickets")
            for (ticket in tickets) {
                // Thêm từng ticket vào collection tickets
                ticketsCollectionRef.document(ticket.id).set(ticket).await()
            }
        } catch (e: Exception) {
            println("Error adding tickets: ${e.message}")
        }
    }

    //update status seat in seatsScheduleSessionTimes
    suspend fun updateSeatStatus(tickets: List<Ticket>) {
        try {
            val seatsScheduleSessionTimesCollectionRef =
                firestore.collection("seatsScheduleSessionTimes")
            tickets.forEach { ticket ->
                val documentRef =
                    seatsScheduleSessionTimesCollectionRef.document(ticket.seatsScheduleSessionTimeId)
                val snapshot = documentRef.get().await()
                if (snapshot.exists()) {
                    // Chuyển document thành đối tượng SeatsScheduleSessionTime
                    val sessionTime = snapshot.toObject(SeatsScheduleSessionTime::class.java)
                    if (sessionTime != null) {
                        // Cập nhật trạng thái của seat tương ứng
                        val updatedSeats = sessionTime.seats.map { seat ->
                            if (seat.id == ticket.seatId) {
                                seat.copy(status = false) // Đổi trạng thái seat
                            } else {
                                seat
                            }
                        }
                        // Ghi lại danh sách seats đã cập nhật vào Firestore
                        documentRef.update("seats", updatedSeats).await()
                    }
                }
            }
        } catch (e: Exception) {
            println("Error adding tickets: ${e.message}")
        }
    }


    suspend fun addReview(review: Review, movieId: String) {
        try {
            val moviesCollectionRef = firestore.collection("movies")
            val movieDocumentRef = moviesCollectionRef.document(movieId)
            //FieldValue.arrayUnion(review) add a new element to the array reviews
            movieDocumentRef.update("reviews", FieldValue.arrayUnion(review))
                .addOnSuccessListener {
                    println("Data added successfully")
                }.addOnFailureListener { e ->
                    println("Error adding data: ${e.message}")
                }
        } catch (e: Exception) {
            println("Error adding data: ${e.message}")
        }
    }


    suspend fun generateBookedTicket(): List<Ticket> {
        return try {
            val seatsScheduleSessionTimesCollectionRef = firestore
                .collection("seatsScheduleSessionTimes")

            val tickets = mutableListOf<Ticket>()

            // Sử dụng await() để chờ dữ liệu được tải
            val documents = seatsScheduleSessionTimesCollectionRef.get().await()

            for (document in documents.documents) {
                val seatsScheduleSessionTime =
                    document.toObject(SeatsScheduleSessionTime::class.java)
                val seats = seatsScheduleSessionTime?.seats
                if (seats != null) {
                    for (seat in seats) {
                        if (!seat.status) {
                            val ticket = Ticket(
                                id = "${seat.id}.${seatsScheduleSessionTime.id}",
                                seatId = seat.id,
                                seatsScheduleSessionTimeId = seatsScheduleSessionTime.id
                            )
                            tickets.add(ticket)
                        }
                    }
                }
            }

            Log.d("ticket", tickets.toString()) // Ghi log danh sách vé
            tickets
        } catch (e: Exception) {
            Log.e("Error", "Failed to generate tickets", e)
            emptyList() // Trả về danh sách rỗng nếu có lỗi
        }
    }

    suspend fun addTicketData() {
        val listTicket = generateBookedTicket()
        val ticketsCollectionRef = firestore.collection("tickets")
        listTicket.forEach { ticket ->
            val ticketDocumentRef = ticketsCollectionRef.document(ticket.id)
            ticketDocumentRef.set(
                mapOf(
                    "id" to ticket.id,
                    "seatId" to ticket.seatId,
                    "seatsScheduleSessionTimeId" to ticket.seatsScheduleSessionTimeId
                )
            )
        }
    }

    fun addAccountData() {
        val accountsCollectionRef = firestore.collection("accounts")
        fakeAccountData.forEach { account ->
            val accountDocumentRef = accountsCollectionRef.document(account.id)
            accountDocumentRef.set(
                mapOf(
                    "id" to account.id,
                    "fullName" to account.fullName,
                    "email" to account.email,
                    "phone" to account.phone,
                    "orders" to account.orders
                )
            )
        }
    }

    fun addRoomData() {
        val roomsCollectionRef = firestore.collection("rooms")
        fakeRoomsData.forEach { room ->
            val roomDocumentRef = roomsCollectionRef.document(room.id)
            roomDocumentRef.set(
                mapOf(
                    "totalSeat" to room.totalSeat,
                    "roomType" to room.roomType,
                    "seats" to room.seats
                )
            ).addOnSuccessListener {
                println("Data added successfully")
            }.addOnFailureListener { e ->
                println("Error adding data: ${e.message}")
            }
        }
    }

    fun addData() {
        val moviesCollectionRef = firestore.collection("movies")
        fakeMoviesData.forEach { movie ->
            val movieDocumentRef = moviesCollectionRef.document(movie.id)
            movieDocumentRef.set(
                mapOf(
                    "name" to movie.name,
                    "information" to movie.information,
                    "movieTypes" to movie.movieTypes,
                    "time" to movie.time,
                    "image" to movie.image,
                    "reviews" to movie.reviews
                )
            ).addOnSuccessListener {
                println("Data added successfully")
            }.addOnFailureListener { e ->
                println("Error adding data: ${e.message}")
            }
        }
    }

    @RequiresApi(35)
    suspend fun generateSeatsScheduleSessionTimeData(): List<SeatsScheduleSessionTime> {
        val seatsRoomMap = mutableMapOf<String, List<Seat>>() // Map lưu ghế theo roomId
        val schedules = mutableListOf<Schedule>() // Lưu danh sách schedule

        // Lấy dữ liệu từ Firestore (rooms)
        val roomsCollectionRef = firestore.collection("rooms")
        val roomsSnapshot = roomsCollectionRef.get().await()  // Chờ Firestore trả về kết quả
        for (document in roomsSnapshot.documents) {
            val room = document.toObject(Room::class.java)?.copy(id = document.id)
            Log.d("Room", " ${room}")
            if (room != null) {
                seatsRoomMap[room.id] = room.seats
            }
        }

        // Lấy dữ liệu từ Firestore (schedules)
        val schedulesCollectionRef = firestore.collection("schedules")
        val schedulesSnapshot =
            schedulesCollectionRef.get().await()  // Chờ Firestore trả về kết quả
        for (document in schedulesSnapshot.documents) {
            val schedule = document.toObject(Schedule::class.java)?.copy(id = document.id)
            if (schedule != null) {
                schedules.add(schedule)
            }
        }

        // Lấy dữ liệu từ Firestore (sessionTimes)
        val result = mutableListOf<SeatsScheduleSessionTime>()
        val sessionTimesCollectionRef = firestore.collection("sessionTimes")
        val sessionTimesSnapshot =
            sessionTimesCollectionRef.get().await()  // Chờ Firestore trả về kết quả
        for (document in sessionTimesSnapshot.documents) {
            val sessionTime = document.toObject(SessionTime::class.java)?.copy(id = document.id)
            if (sessionTime != null) {
                for (scheduleId in sessionTime.scheduleIds) {
                    val schedule = schedules.find { it.id == scheduleId }
                    if (schedule != null) {
                        val seats =
                            seatsRoomMap[schedule.roomId] ?: emptyList() // Sao chép danh sách ghế
                        result.add(
                            SeatsScheduleSessionTime(
                                sessionTimeId = sessionTime.id,
                                scheduleId = schedule.id,
                                seats = seats
                            )
                        )
                    }
                }
            }
        }

        return result
    }

    suspend fun updateSeatsScheduleSessionTimesWithId() {
        try {
            val collectionRef = firestore.collection("seatsScheduleSessionTimes")

            // Lấy tất cả các documents trong collection
            val documents = collectionRef.get().await()

            for (document in documents) {
                // Lấy document ID
                val documentId = document.id

                // Cập nhật field 'id' với giá trị là documentId
                collectionRef.document(documentId).update("id", documentId).await()
            }

            println("Successfully updated all documents with id field!")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to update documents: ${e.message}")
        }
    }


    @RequiresApi(35)
    suspend fun addSeatsScheduleSessionTime() {
        val seatsScheduleSessionTimes = generateSeatsScheduleSessionTimeData()

        val seatsScheduleSessionTimesCollectionRef =
            firestore.collection("seatsScheduleSessionTimes")
        seatsScheduleSessionTimes.forEach { seatsScheduleSessionTime ->
            val seatsScheduleSessionTimeDocumentRef = seatsScheduleSessionTimesCollectionRef
                .document(seatsScheduleSessionTime.sessionTimeId + "." +
                        seatsScheduleSessionTime.scheduleId)
            seatsScheduleSessionTimeDocumentRef.set(
                mapOf(
                    "sessionTimeId" to seatsScheduleSessionTime.sessionTimeId,
                    "scheduleId" to seatsScheduleSessionTime.scheduleId,
                    "seats" to seatsScheduleSessionTime.seats
                )
            )
        }
    }

    suspend fun updateSessionData() {
        try {
            val collectionRef = firestore.collection("sessionTimes")
            val documents = collectionRef.get().await()
            for (document in documents) {
                val documentId = document.id
                collectionRef.document(documentId).update("id", documentId).await()
            }

            println("Successfully updated all documents with id field!")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to update documents: ${e.message}")
        }
    }

    suspend fun updateScheduleData() {
        try {
            val collectionRef = firestore.collection("schedules")
            val documents = collectionRef.get().await()
            for (document in documents) {
                val documentId = document.id
                collectionRef.document(documentId).update("id", documentId).await()
            }
            println("Successfully updated all documents with id field!")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to update documents: ${e.message}")
        }
    }

    suspend fun updateRoomsData() {
        try {
            val collectionRef = firestore.collection("rooms")
            val documents = collectionRef.get().await()
            for (document in documents) {
                val documentId = document.id
                collectionRef.document(documentId).update("id", documentId).await()
            }
            println("Successfully updated all documents with id field!")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to update documents: ${e.message}")
        }
    }

    suspend fun updateMoviesData() {
        try {
            val collectionRef = firestore.collection("movies")
            val documents = collectionRef.get().await()
            for (document in documents) {
                val documentId = document.id
                collectionRef.document(documentId).update("id", documentId).await()
            }
            println("Successfully updated all documents with id field!")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to update documents: ${e.message}")
        }
    }

    //        schedulesCollectionRef.get().addOnSuccessListener { querySnapshot ->
//            val batch = firestore.batch()
//            for (document in querySnapshot.documents) {
//                batch.delete(document.reference) // Xóa từng document
//            }
//            batch.commit()
//                .addOnSuccessListener {
//                    println("Collection has been deleted.")
//                }
//                .addOnFailureListener { e ->
//                    println("Error deleting collection  ${e.message}")
//                }
//        }

    fun addScheduleData() {
        val sessionTimesCollectionRef = firestore.collection("sessionTimes")
        fakeSessionTimes.forEach { sessionTime ->
            val sessionTimeDocumentRef = sessionTimesCollectionRef.document(sessionTime.id)
            sessionTimeDocumentRef.set(
                mapOf(
                    "time" to sessionTime.time,
                    "price" to sessionTime.price,
                    "scheduleIds" to sessionTime.scheduleIds
                )
            )
        }

        val schedulesCollectionRef = firestore.collection("schedules")
        fakeSchedules.forEach { schedule ->
            val scheduleDocumentRef = schedulesCollectionRef.document(schedule.id)
            scheduleDocumentRef.set(
                mapOf(
                    "day" to schedule.day,
                    "movieId" to schedule.movieId,
                    "roomId" to schedule.roomId
                )
            )
        }
    }

}



