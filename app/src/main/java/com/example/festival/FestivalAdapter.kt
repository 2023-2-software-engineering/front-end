package com.example.festival

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FestivalAdapter(private var festivals: List<Festival>): RecyclerView.Adapter<FestivalAdapter.FestivalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.festival_recyclerview, parent, false)
        return FestivalViewHolder(view)
    }

    override fun onBindViewHolder(holder: FestivalViewHolder, position: Int) {
        val festival = festivals[position]
        holder.bind(festival)
    }

    override fun getItemCount(): Int {
       return festivals.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newFestivals: List<Festival>) {
        festivals = newFestivals
        notifyDataSetChanged()
    }

    inner class FestivalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.festival_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.festival_date)
        private val placeTextView: TextView = itemView.findViewById(R.id.festival_place)
        private val imageView: ImageView = itemView.findViewById(R.id.main_img)

        init {
            itemView.setOnClickListener {
                val clickedFestival = festivals[adapterPosition]
                val festivalId = clickedFestival.festivalId // 클릭된 페스티벌의 Id를 가져옴
                val intent = Intent(itemView.context, FestivalDetailActivity::class.java)
                intent.putExtra("festivalId", festivalId)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(festivalList: Festival) {
            titleTextView.text = festivalList.title
            dateTextView.text = "${festivalList.date}"
            placeTextView.text = festivalList.location
        }

    }
}