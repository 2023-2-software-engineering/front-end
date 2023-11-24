package com.example.festival

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(private var comments: List<CommentListResponse>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_recyclerview, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newComments: List<CommentListResponse>) {
        comments = newComments
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.comment_writer)
        private val userAddressTextView: TextView = itemView.findViewById(R.id.comment_address)
        private val commentTextView: TextView = itemView.findViewById(R.id.comment_text)
        private val createdTextView: TextView = itemView.findViewById(R.id.comment_date)
        private val writerImage: ImageView = itemView.findViewById(R.id.writer_img)

        fun bind(comment: CommentListResponse) {
            usernameTextView.text = comment.nickname
            userAddressTextView.text = comment.address
            commentTextView.text = comment.content

            Glide.with(itemView.context)
                .load(comment.userimage)
                .placeholder(R.drawable.user_basic) // 플레이스홀더 이미지 리소스
                .error(R.drawable.user_basic) // 에러 이미지 리소스
                .into(writerImage)

            // 댓글 날짜 시간도 출력 수정 필요(현재 날짜만 보임)
            val parts = comment.createdAt.split("T")

            if (parts.size == 2) {
                val datePart = parts[0]
                val timeWithMillisPart = parts[1]

                // 밀리초 부분을 제외한 시간 부분 추출
                val timePart = timeWithMillisPart.substring(0, 8)

                // 날짜와 시간을 조합하여 Timestamp로 변환
                val timestampString = "$datePart $timePart"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val parsedTimestamp = Timestamp(dateFormat.parse(timestampString).time)

                // SimpleDateFormat을 사용하여 원하는 형식으로 포맷
                val outputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                outputDateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul") // 원하는 시간대 설정
                val formattedDate = outputDateFormat.format(parsedTimestamp)

                // formattedDate를 TextView에 설정
                createdTextView.text = formattedDate
                // 결과를 출력
                Log.d("Formatted Date", formattedDate)
            } else {
                // 올바른 형식이 아닐 경우 오류 처리
                Log.e("Error", "Invalid timestamp format")
            }
        }
    }
}