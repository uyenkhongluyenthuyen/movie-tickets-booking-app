package com.example.bookingmovietickets.utils

import com.example.bookingmovietickets.data.model.lichchieu.Schedule
import com.example.bookingmovietickets.data.model.lichchieu.SessionTime
import com.example.bookingmovietickets.data.model.phim.Movie
import com.example.bookingmovietickets.data.model.phim.MovieType
import com.example.bookingmovietickets.data.model.phim.Review
import com.example.bookingmovietickets.data.model.phong.Room
import com.example.bookingmovietickets.data.model.phong.RoomType
import com.example.bookingmovietickets.data.model.phong.Seat
import com.example.bookingmovietickets.data.model.taikhoan.Account




val fakeAccountData = listOf(
    Account(
        id = "account_1",
        fullName = "Nguyễn Thị Thu Hiền",
        email = "hien02022003@gmail.com",
        phone = "0901234567",
        orders = emptyList()
    ),
    Account(
        id = "account_2",
        fullName = "Vũ Thị Thu Hà",
        email = "havu@gmail.com",
        phone = "0901234567",
        orders = emptyList()
    ),
    Account(
        id = "account_3",
        fullName = "Nguyễn Ngọc Hảo",
        email = "nguyenhaongoc666@gmail.com",
        phone = "0901234567",
        orders = emptyList()
    ),
    Account(
        id = "account_4",
        fullName = "Đào Minh Quang",
        email = "quangpeach92@gmail.com",
        phone = "0901234567",
        orders = emptyList()
    ),Account(
        id = "account_5",
        fullName = "Nguyễn Thị Thu Uyên",
        email = "uyennguyenthithu322003@gmail.com",
        phone = "0901234567",
        orders = emptyList()
    )
)


val fakeMoviesData = listOf(
    Movie(
        id = "movie_1",
        name = "Avengers: Endgame",
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
    ),
    Movie(
        id = "movie_2",
        name = "Inception",
        information = "Cuộc hành trình vào trong giấc mơ và đánh cắp ý tưởng.",
        movieTypes = listOf(
            MovieType(
                id = "type_3",
                name = "Hài kịch"
            ),
            MovieType(
                id = "type_4",
                name = "Giải trí"
            )
        ),
        time = 148,
        image = "https://example.com/inception.jpg",
        reviews = listOf(
            Review(
                id = "m2_review_3",
                accountId = "account_3",
                content = "Phim rất khó hiểu, nhưng tuyệt vời!",
                rate = 5
            )
        )
    ),
    Movie(
        id = "movie_3",
        name = "The Dark Knight",
        information = "Hiệp sĩ bóng đêm đối đầu với Joker để bảo vệ Gotham.",
        movieTypes = listOf(
            MovieType(
                id = "type_3",
                name = "Hài kịch"
            ),
            MovieType(
                id = "type_4",
                name = "Giải trí"
            )
        ),
        time = 152,
        image = "https://example.com/dark_knight.jpg",
        reviews = listOf(
            Review(
                id = "m3_review_4",
                accountId = "account_4",
                content = "Diễn xuất của Heath Ledger thật sự xuất sắc!",
                rate = 5
            )
        )
    ),
    Movie(
        id = "movie_4",
        name = "Interstellar",
        information = "Cuộc hành trình đến các hành tinh xa xôi để cứu nhân loại.",
        movieTypes = listOf(
            MovieType(
                id = "type_3",
                name = "Hài kịch"
            ),
            MovieType(
                id = "type_4",
                name = "Giải trí"
            )
        ),
        time = 169,
        image = "https://example.com/interstellar.jpg",
        reviews = listOf(
            Review(
                id = "m4_review_5",
                accountId = "account_5",
                content = "Một tuyệt tác về không gian và thời gian.",
                rate = 5
            )
        )
    ),
    Movie(
        id = "movie_5",
        name = "Parasite",
        information = "Câu chuyện châm biếm xã hội giữa hai gia đình ở Hàn Quốc.",
        movieTypes = listOf(
            MovieType(
                id = "type_3",
                name = "Hài kịch"
            ),
            MovieType(
                id = "type_4",
                name = "Giải trí"
            )
        ),
        time = 132,
        image = "https://example.com/parasite.jpg",
        reviews = listOf(
            Review(
                id = "m5_review_6",
                accountId = "account_6",
                content = "Phim lột tả sự phân hóa giai cấp rất sâu sắc.",
                rate = 5
            ),
            Review(
                id = "m5_review_7",
                accountId = "taiKhoan_7",
                content = "Cốt truyện đầy bất ngờ và lôi cuốn.",
                rate = 4
            )
        )
    )
)


val seatList = listOf(
    Seat(id = "room_1_seat_1", seatNumber = "1", type = "A", status = true),
    Seat(id = "room_1_seat_2", seatNumber = "2", type = "B", status = true),
    Seat(id = "room_1_seat_3", seatNumber = "3", type = "C", status = true),
    Seat(id = "room_1_seat_4", seatNumber = "4", type = "D", status = true),
    Seat(id = "room_1_seat_5", seatNumber = "5", type = "E", status = true),
    Seat(id = "room_1_seat_6", seatNumber = "6", type = "A", status = true),
    Seat(id = "room_1_seat_7", seatNumber = "7", type = "B", status = true),
    Seat(id = "room_1_seat_8", seatNumber = "8", type = "C", status = true),
    Seat(id = "room_1_seat_9", seatNumber = "9", type = "D", status = true),
    Seat(id = "room_1_seat_10", seatNumber = "10", type = "E", status = false),
    Seat(id = "room_1_seat_11", seatNumber = "11", type = "A", status = false),
    Seat(id = "room_1_seat_12", seatNumber = "12", type = "B", status = true),
    Seat(id = "room_1_seat_13", seatNumber = "13", type = "C", status = true),
    Seat(id = "room_1_seat_14", seatNumber = "14", type = "D", status = true),
    Seat(id = "room_1_seat_15", seatNumber = "15", type = "E", status = true),
    Seat(id = "room_1_seat_16", seatNumber = "16", type = "A", status = true),
    Seat(id = "room_1_seat_17", seatNumber = "17", type = "B", status = true),
    Seat(id = "room_1_seat_18", seatNumber = "18", type = "C", status = true),
    Seat(id = "room_1_seat_19", seatNumber = "19", type = "D", status = true),
    Seat(id = "room_1_seat_20", seatNumber = "20", type = "E", status = true),
    Seat(id = "room_1_seat_21", seatNumber = "21", type = "A", status = true),
    Seat(id = "room_1_seat_22", seatNumber = "22", type = "B", status = true),
    Seat(id = "room_1_seat_23", seatNumber = "23", type = "C", status = true),
    Seat(id = "room_1_seat_24", seatNumber = "24", type = "D", status = true),
    Seat(id = "room_1_seat_25", seatNumber = "25", type = "E", status = true),
    Seat(id = "room_1_seat_26", seatNumber = "26", type = "A", status = true),
    Seat(id = "room_1_seat_27", seatNumber = "27", type = "B", status = true),
    Seat(id = "room_1_seat_28", seatNumber = "28", type = "C", status = true),
    Seat(id = "room_1_seat_29", seatNumber = "29", type = "D", status = true),
    Seat(id = "room_1_seat_30", seatNumber = "30", type = "E", status = true),
    Seat(id = "room_1_seat_31", seatNumber = "31", type = "A", status = true),
    Seat(id = "room_1_seat_32", seatNumber = "32", type = "B", status = true),
    Seat(id = "room_1_seat_33", seatNumber = "33", type = "C", status = true),
    Seat(id = "room_1_seat_34", seatNumber = "34", type = "D", status = true),
    Seat(id = "room_1_seat_35", seatNumber = "35", type = "E", status = true),
    Seat(id = "room_1_seat_36", seatNumber = "36", type = "A", status = true),
    Seat(id = "room_1_seat_37", seatNumber = "37", type = "B", status = true),
    Seat(id = "room_1_seat_38", seatNumber = "38", type = "C", status = true),
    Seat(id = "room_1_seat_39", seatNumber = "39", type = "D", status = true),
    Seat(id = "room_1_seat_40", seatNumber = "40", type = "E", status = true)
)

val fakeSessionTimes = listOf(
    SessionTime(
        id = "session_1",
        time = "10:00",
        price = 50.0,
        scheduleIds = listOf("schedule_movie_1_room_1_241124", "schedule_movie_2_room_3_231124")
    ),
    SessionTime(
        id = "session_2",
        time = "13:00",
        price = 60.0,
        scheduleIds = listOf(
            "schedule_movie_1_room_1_241124",
            "schedule_movie_2_room_3_231124",
            "schedule_movie_3_room_4_231124"
        )
    ),
    SessionTime(
        id = "session_3",
        time = "16:00",
        price = 70.0,
        scheduleIds = listOf("schedule_movie_1_room_2_251124", "schedule_movie_3_room_4_231124")
    ),
    SessionTime(
        id = "session_4",
        time = "19:00",
        price = 80.0,
        scheduleIds = listOf(
            "schedule_movie_1_room_1_241124",
            "schedule_movie_3_room_4_231124",
            "schedule_movie_4_room_4_251124"
        )
    ),
    SessionTime(
        id = "session_5",
        time = "22:00",
        price = 90.0,
        scheduleIds = listOf("schedule_movie_1_room_2_251124", "schedule_movie_4_room_4_251124")
    )
)


val fakeSchedules = listOf(
    Schedule(
        id = "schedule_movie_1_room_1_241124",
        day = "24/11/2024",
        movieId = "movie_1",
        roomId = "room_1"
    ),
    Schedule(
        id = "schedule_movie_1_room_2_251124",
        day = "25/11/2024",
        movieId = "movie_1",
        roomId = "room_2"
    ),
    Schedule(
        id = "schedule_movie_2_room_3_231124",
        day = "23/11/2024",
        movieId = "movie_2",
        roomId = "room_3"
    ),
    Schedule(
        id = "schedule_movie_3_room_4_231124",
        day = "23/11/2024",
        movieId = "movie_3",
        roomId = "room_4"
    ),
    Schedule(
        id = "schedule_movie_4_room_4_251124",
        day = "25/11/2024",
        movieId = "movie_4",
        roomId = "room_5"
    )
)


val fakeRoomsData = listOf(
    Room(
        id = "room_1",
        totalSeat = 50,
        roomType = RoomType(
            id = "room_type_1",
            roomType = "Standard",
            information = "Phòng tiêu chuẩn với 50 ghế"
        ),
        seats = generateFakeSeats(50, "room_1")
    ),
    Room(
        id = "room_2",
        totalSeat = 40,
        roomType = RoomType(
            id = "room_type_2",
            roomType = "VIP",
            information = "Phòng VIP với 40 ghế, chỗ ngồi thoải mái hơn"
        ),
        seats = generateFakeSeats(40, "room_2")
    ),
    Room(
        id = "room_3",
        totalSeat = 40,
        roomType = RoomType(
            id = "room_type_2",
            roomType = "VIP",
            information = "Phòng VIP với 40 ghế, chỗ ngồi thoải mái hơn"
        ),
        seats = generateFakeSeats(40, "room_2")
    ),
    Room(
        id = "room_4",
        totalSeat = 50,
        roomType = RoomType(
            id = "room_type_1",
            roomType = "Standard",
            information = "Phòng tiêu chuẩn với 50 ghế"
        ),
        seats = generateFakeSeats(40, "room_2")
    ),
    Room(
        id = "room_5",
        totalSeat = 50,
        roomType = RoomType(
            id = "room_type_1",
            roomType = "Standard",
            information = "Phòng tiêu chuẩn với 50 ghế"
        ),
        seats = generateFakeSeats(40, "room_2")
    )
)