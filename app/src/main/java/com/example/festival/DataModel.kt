package com.example.festival

import java.sql.Date

data class Festival(
    val festivalId: Int,
    val title: String,
    val content: String,
    val date: Date,
    val place: String
)

// 게시판 작성
data class Board(
    val title: String,
    val content: String
)

data class BoardData(
    val partnerId: Int,
    val title: String,
    val content: String,
    val createAt: Date,
    val count: Int,
    val nickname: String,
    val address: String
)