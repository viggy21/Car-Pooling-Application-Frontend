package com.example.carpoolingapplicationfrontend.features.booking

object FakeBookingData{
    val bookings = listOf(
        BookingDto(
            id = 1,
            tripId = 101,
            userId = 7,
            role = 0,
            originName = "Monash University Clayton",
            originLat = -37.9105,
            originLng = 145.1364,
            destName = "Melbourne CBD",
            destLat = -37.8136,
            destLng = 144.9631,
            expectedStartTime = "2026-04-21T08:30:00Z",
            status = 1,
            remark = "Near library entrance",
            createTime = "2026-04-20T10:00:00Z"
        ),

        BookingDto(
            id = 2,
            tripId = 102,
            userId = 7,
            role = 1,
            originName = "Chadstone Shopping Centre",
            originLat = -37.8860,
            originLng = 145.0825,
            destName = "Flinders Street Station",
            destLat = -37.8183,
            destLng = 144.9671,
            expectedStartTime = "2026-04-21T12:00:00Z",
            status = 0,
            remark = null,
            createTime = "2026-04-20T11:00:00Z"
        ),

        BookingDto(
            id = 3,
            tripId = 103,
            userId = 7,
            role = 0,
            originName = "South Yarra Station",
            originLat = -37.8382,
            originLng = 144.9924,
            destName = "Docklands",
            destLat = -37.8170,
            destLng = 144.9460,
            expectedStartTime = "2026-04-22T09:15:00Z",
            status = 2,
            remark = "Running 5 min late",
            createTime = "2026-04-20T12:00:00Z"
        )
    )
}
