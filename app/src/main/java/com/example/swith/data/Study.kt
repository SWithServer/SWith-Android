package com.example.swith.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class Study(
 var title: String,
    var category: String,
    var totalPeople: Int,
    var totalRound: Int,
)
