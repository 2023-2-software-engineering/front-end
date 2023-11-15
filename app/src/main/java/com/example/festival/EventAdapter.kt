package com.example.festival

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private var events: List<Event>): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
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
            if (eventList.ing == 0) {
                ingTextView.visibility = View.GONE
                closeTextView.visibility = View.VISIBLE
            }
        }

    }
}