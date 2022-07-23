package com.example.swith.data

data class Round(
    val count: Int,
    var startTime: DateTime,
    var endTime: DateTime,
    var detail: String,
    var isOnline: Boolean,
    var place: String?,
    val attend: Int
)
