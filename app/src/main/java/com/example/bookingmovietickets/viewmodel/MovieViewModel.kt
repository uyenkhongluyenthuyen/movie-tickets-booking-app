package com.example.bookingmovietickets.viewmodel

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingmovietickets.data.model.ResultResponse
import com.example.bookingmovietickets.data.model.SeatsScheduleSessionTime
import com.example.bookingmovietickets.data.model.lichchieu.Schedule
import com.example.bookingmovietickets.data.model.lichchieu.SessionTime
import com.example.bookingmovietickets.data.model.phim.Movie
import com.example.bookingmovietickets.data.model.phim.Review
import com.example.bookingmovietickets.data.model.taikhoan.Order
import com.example.bookingmovietickets.data.model.taikhoan.Ticket
import com.example.bookingmovietickets.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: FirebaseRepository) : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: MutableStateFlow<List<Movie>> = _movies

    private val _movieSelected = MutableStateFlow<Movie>(Movie())
    val movieSelected: MutableStateFlow<Movie> = _movieSelected

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules: MutableStateFlow<List<Schedule>> = _schedules

    private val _sessionTimes = MutableStateFlow<List<SessionTime>>(emptyList())
    val sessionTimes: MutableStateFlow<List<SessionTime>> = _sessionTimes

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> = _loading

    private val _seatsScheduleSessionTime =
        MutableStateFlow<SeatsScheduleSessionTime>(SeatsScheduleSessionTime())
    val seatsScheduleSessionTime: MutableStateFlow<SeatsScheduleSessionTime> =
        _seatsScheduleSessionTime

    private val _loadingSeats = MutableStateFlow(false)
    val loadingSeats: MutableStateFlow<Boolean> = _loadingSeats


    private val _updateOrderState = MutableStateFlow(false)
    val updateOrderState: MutableStateFlow<Boolean> = _updateOrderState

    fun resetData() {
        _movieSelected.value = Movie()
        _loading.value = false
        _sessionTimes.value = emptyList()
        _seatsScheduleSessionTime.value = SeatsScheduleSessionTime()
        _updateOrderState.value = false
    }

    fun fetchMovies() {
        viewModelScope.launch {
            when (val response = repository.getMovies()) {
                is ResultResponse.Success -> {
                    _movies.value = response.data ?: emptyList()
                }

                is ResultResponse.Error -> {
                    Log.e("MovieViewModel", "Error fetching movies: ${response.exception}")
                }

                else -> Unit
            }
        }
    }

    fun fetchMovieById(movieId: String) {
        viewModelScope.launch {
            when (val response = repository.getMovieById(movieId)) {
                is ResultResponse.Success -> {
                    _movieSelected.value = response.data ?: Movie()
                }

                is ResultResponse.Error -> {
                    Log.e("MovieViewModel", "Error fetching movie by ID: ${response.exception}")
                }

                else -> Unit
            }
        }
    }

    fun fetchSessionTimesByMovieAndDay(movieId: String, day: String) {
        viewModelScope.launch {
            _loading.value = true

            // Bước 1: Lấy danh sách Schedule
            when (val scheduleResponse = repository.fetchSchedulesByMovieAndDay(movieId, day)) {
                is ResultResponse.Success -> {
                    val schedules = scheduleResponse.data ?: emptyList()
                    _schedules.value = schedules

                    // Lấy danh sách scheduleIds
                    val scheduleIds = schedules.map { it.id }

                    if (scheduleIds.isNotEmpty()) {
                        // Bước 2: Lấy danh sách SessionTime theo scheduleIds nếu scheduleIds không rỗng
                        when (val sessionTimesResponse =
                            repository.fetchSessionTimesByScheduleIds(scheduleIds)) {
                            is ResultResponse.Success -> _sessionTimes.value =
                                sessionTimesResponse.data ?: emptyList()

                            is ResultResponse.Error -> Log.e(
                                "MovieViewModel",
                                "Error fetching session times: ${sessionTimesResponse.exception}"
                            )
                        }
                    } else {
                        // Nếu không có scheduleIds, đặt sessionTimes là rỗng
                        _sessionTimes.value = emptyList()
                        Log.w(
                            "MovieViewModel",
                            "No scheduleIds found for movieId=$movieId and day=$day"
                        )
                    }
                }

                is ResultResponse.Error -> Log.e(
                    "MovieViewModel",
                    "Error fetching schedules: ${scheduleResponse.exception}"
                )
            }
            _loading.value = false
        }
    }

    fun fetchSeatsScheduleSessionTime(
        time: String,
        movieId: String,
        date: String,
        sessionTimeId: String
    ) {
        viewModelScope.launch {
            _loadingSeats.value = true

            try {
                // Gọi hàm để lấy scheduleId
                when (val scheduleIdResponse =
                    repository.fetchScheduleIdBySessionTimeId(time, movieId, date)) {
                    is ResultResponse.Success -> {
                        val scheduleId = scheduleIdResponse.data

                        // Dùng scheduleId để gọi hàm fetchSeatsScheduleSessionTimes
                        when (val response = repository.fetchSeatsScheduleSessionTimes(
                            scheduleId.toString(),
                            sessionTimeId
                        )) {
                            is ResultResponse.Success -> {
                                _seatsScheduleSessionTime.value =
                                    response.data ?: SeatsScheduleSessionTime()
                            }

                            is ResultResponse.Error -> {
                                _seatsScheduleSessionTime.value = SeatsScheduleSessionTime()
                                Log.e(
                                    "MovieViewModel",
                                    "Error fetching seats: ${response.exception}"
                                )
                            }
                        }
                    }

                    is ResultResponse.Error -> {
                        _seatsScheduleSessionTime.value = SeatsScheduleSessionTime()
                        Log.e(
                            "MovieViewModel",
                            "Error fetching scheduleId: ${scheduleIdResponse.exception}"
                        )
                    }
                }
            } catch (e: Exception) {
                _seatsScheduleSessionTime.value = SeatsScheduleSessionTime()
                Log.e("MovieViewModel", "Unexpected error occurred: $e")
            }

            _loadingSeats.value = false
        }
    }

    fun resetSeatsScheduleSessionTime() {
        _seatsScheduleSessionTime.value = SeatsScheduleSessionTime()
    }


    @RequiresApi(35)
    fun buyTicket(accountId: String, order: Order, tickets: List<Ticket>) {
        viewModelScope.launch {
            try {
                // 1. Update orders
                when (val orderResponse = repository.updateOrderInListOrders(accountId, order)) {
                    is ResultResponse.Success -> {
                        Log.d("MovieViewModel", "Order updated successfully")
                        // 2. Update tickets sau khi order thành công
                        repository.updateTickets(tickets)

                        //3. update status seat
                        repository.updateSeatStatus(tickets)
                        Log.d("MovieViewModel", "Tickets updated successfully")

                        _updateOrderState.value = true
                        Log.d("OrderState1", "${updateOrderState.value}")
                    }

                    is ResultResponse.Error -> {
                        Log.e("MovieViewModel", "Error updating order: ${orderResponse.exception}")
                        _updateOrderState.value = false
                        Log.d("OrderState2", "${updateOrderState.value}")

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _updateOrderState.value = false
                Log.d("OrderState3", "${updateOrderState.value}")

            }
        }
    }


    fun addReviewToFireStore(review: Review, movieId: String) {
        viewModelScope.launch {
            repository.addReview(review, movieId)
        }
    }

    // Thêm phương thức để thêm dữ liệu vào FireStore
    fun addMovieDataToFireStore() {
        repository.addData()
    }

    fun addRoomsDataToFireStore() {
        repository.addRoomData()
    }

    fun addScheduleDataToFireStore() {
        repository.addScheduleData()

    }

    fun addAccountDataToFireStore() {
        repository.addAccountData()
    }

    fun generateTicket() {
        viewModelScope.launch {
            try {
                repository.addTicketData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateSeatsScheduleSessionTime() {
        viewModelScope.launch {
            try {
                repository.updateSeatsScheduleSessionTimesWithId()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateSessionTime() {
        viewModelScope.launch {
            try {
                repository.updateSessionData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateSchedule() {
        viewModelScope.launch {
            try {
                repository.updateScheduleData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateRoom() {
        viewModelScope.launch {
            try {
                repository.updateRoomsData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateMovies() {
        viewModelScope.launch {
            try {
                repository.updateMoviesData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // Hàm để gọi `addSeatsScheduleSessionTime`
    @RequiresApi(35)
    fun addSeatsScheduleSessionTime() {
        viewModelScope.launch {
            try {
                repository.addSeatsScheduleSessionTime()
                // Xử lý thành công
            } catch (e: Exception) {
                // Xử lý lỗi
                e.printStackTrace()
            }
        }
    }

}