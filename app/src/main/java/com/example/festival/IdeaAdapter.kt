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

class IdeaAdapter(private var ideas: List<IdeaData>) : RecyclerView.Adapter<IdeaAdapter.IdeaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.idea_recyclerview, parent, false)
        return IdeaViewHolder(view)
    }

    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        val idea = ideas[position]
        holder.bind(idea)
    }

    override fun getItemCount(): Int {
        return ideas.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newIdeas: List<IdeaData>) {
        ideas = newIdeas
        notifyDataSetChanged()
    }

    inner class IdeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.idea_title)
        private val writerTextView: TextView = itemView.findViewById(R.id.idea_writer)
        private val createdTextView: TextView = itemView.findViewById(R.id.idea_date)

        init {
            itemView.setOnClickListener {
                val clickedIdea = ideas[adapterPosition]
                val ideaId = clickedIdea.ideaId  // 클릭된 Id를 가져옴
                val intent = Intent(itemView.context, IdeaDetailActivity::class.java)
                intent.putExtra("ideaId", ideaId)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(ideaList: IdeaData) {
            titleTextView.text = ideaList.title
            writerTextView.text = ideaList.user.nickname

            val parts = ideaList.createdAt.split("T")

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