package com.example.festival

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class BoardAdapter(private var boards: List<BoardData>) : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_recyclerview, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val board = boards[position]
        holder.bind(board)
    }

    override fun getItemCount(): Int {
        return boards.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newBoards: List<BoardData>) {
        boards = newBoards
        notifyDataSetChanged()
    }

    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.board_title)
        private val writerTextView: TextView = itemView.findViewById(R.id.board_writer)
        private val createdTextView: TextView = itemView.findViewById(R.id.board_date)
        private val commentCount: TextView = itemView.findViewById(R.id.comment_num)

        init {
            itemView.setOnClickListener {
                val clickedBoard = boards[adapterPosition]
                val boardId = clickedBoard.partnerId  // 클릭된 게시판의 Id를 가져옴
                val intent = Intent(itemView.context, BoardDetailActivity::class.java)
                intent.putExtra("boardId", boardId)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(boardList: BoardData) {
            titleTextView.text = boardList.title
            writerTextView.text = boardList.nickname
            commentCount.text = "${boardList.count}"

            val parts = boardList.createdAt.split("T")

            if (parts.size == 2) {
                val datePart = parts[0]
                val timeWithMillisPart = parts[1]

                // 밀리초 부분을 제외한 시간 부분 추출
                val timePart = timeWithMillisPart.substring(0, 8)

                // 날짜와 시간을 조합하여 Timestamp로 변환
                val timestampString = "$datePart"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                createdTextView.text = timestampString
                // 결과를 출력
                Log.d("Formatted Date", timestampString)
            } else {
                // 올바른 형식이 아닐 경우 오류 처리
                Log.e("Error", "Invalid timestamp format")
            }
        }
    }
}