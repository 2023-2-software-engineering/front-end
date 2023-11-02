package com.example.festival

import java.sql.Date

data class Festival(
    val festivalId: Int,
    val title: String,
    val content: String,
    val date: Date,
    val place: String
)
