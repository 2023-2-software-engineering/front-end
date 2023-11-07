package com.example.festival

import java.sql.Date

data class Login(
    val identify: String,
    val password: String
)

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
    val createdAt: String,
    val count: Int,
    val nickname: String,
    val address: String
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
    val content: String
)

data class ReportData(
    val reportId: Int,
    val nickname: String,
    val address: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val done: Boolean
)