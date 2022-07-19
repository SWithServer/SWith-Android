package com.example.swith.data

data class Round(
    val count: Int,
    var startTime: String,
    var endTime: String,
    var detail: String,
    var isOnline: Boolean,
    var place: String?,
    val attend: Int
)
