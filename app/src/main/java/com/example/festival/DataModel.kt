package com.example.festival

import java.sql.Date

data class Login(
    val identify: String,
    val password: String
)

data class Join(
    val username: String,
    val nickname: String,
    val identify: String,
    val address: String,
    val password: String,
    val phoneNumber: String
)

data class User(
    val userId: Int,
    val username: String,
    val identify: String,
    val nickname: String,
    val password: String,
    val address: String,
    val phoneNumber: String
)

data class UserUpdate(
    val username: String,
    val address: String,
)

data class Festival(
    val festivalId: Int,
    val title: String,
    val content: String,
    val image: String,
    val location: String,
    val date: Date
)

data class Event(
    val eventId: Int,
    val title: String,
    val content: String,
    val image: String,
    val location: String,
    val rule: String,
    val register: String,
    val ing: Int,
    val startDay: String,
    val endDay: String,
    val resultDay: String
)

// 게시판 작성
data class Board(
    val title: String,
    val content: String,
    val festivalId: Int
)

data class BoardData(
    val partnerId: Int,
    val title: String,
    val content: String,
    val createdAt: String,
    val count: Int,
    val nickname: String,
    val address: String,
    val festivalId: Int,
    val image: String
)

// 댓글 작성
data class Comment(
    val content: String
)

// 댓글 조회
data class CommentListResponse(
    val commentId: Int,
    val nickname: String,
    val address: String,
    val partnerId: Int,
    val content: String,
    val createdAt: String,
    val replyDtos: List<ReplyList>
)

data class ReplyList(
    val replyId: Int,
    val content: String,
    val nickname: String,
    val address: String,
    val commentId: Int,
    val createdAt: String
)

// 신고 작성
data class Report(
    val title: String,
    val content: String,
    val festivalId: Int
)

data class ReportData(
    val reportId: Int,
    val nickname: String,
    val address: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val done: Boolean,
    val image: String,
    val festivalId: Int
)

data class Main(
    val festivalId: Int,
    val title: String,
    val image: String ?= null,
)