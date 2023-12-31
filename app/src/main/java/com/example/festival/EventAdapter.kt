package com.example.festival

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EventAdapter(private var events: List<Event>): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    var authToken: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_recyclerview, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.event_title)
        private val startTextView: TextView = itemView.findViewById(R.id.event_start)
        private val endTextView: TextView = itemView.findViewById(R.id.event_end)
        private val ingTextView: TextView = itemView.findViewById(R.id.event_ing)
        private val closeTextView: TextView = itemView.findViewById(R.id.event_close)
        private val imageView: ImageView = itemView.findViewById(R.id.main_img)
        private val likeTextView: TextView = itemView.findViewById(R.id.event_like_img)

        init {
            itemView.setOnClickListener {
                val clickedFestival = events[adapterPosition]
                val eventId = clickedFestival.eventId // 클릭된 Id를 가져옴
                val intent = Intent(itemView.context, EventDetailActivity::class.java)
                intent.putExtra("eventId", eventId)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(eventList: Event) {
            titleTextView.text = eventList.title
            startTextView.text = "${eventList.startDay}"
            endTextView.text = "${eventList.endDay}"

            if (authToken != null) {
                EventManager.getEventLikeCheck(
                    eventList.eventId, authToken!!,
                    onSuccess = { isMe ->
                        if (isMe == 1) {
                            likeTextView.visibility = View.VISIBLE
                        }
                    },
                    onError = { throwable ->
                        Log.e("서버 테스트3", "오류: $throwable")
                    }
                )
            }

            Glide.with(itemView.context)
                .load(eventList.image)
                .placeholder(R.drawable.festival_main) // 플레이스홀더 이미지 리소스
                .error(R.drawable.festival_main) // 에러 이미지 리소스
                .into(imageView)

            when (eventList.state) {
                0 -> {
                    ingTextView.visibility = View.GONE
                    closeTextView.visibility = View.VISIBLE
                }
                1 -> {
                    ingTextView.visibility = View.VISIBLE
                    closeTextView.visibility = View.GONE
                }
                else -> {
                    ingTextView.visibility = View.GONE
                    closeTextView.visibility = View.GONE
                }
            }
        }

    }
}