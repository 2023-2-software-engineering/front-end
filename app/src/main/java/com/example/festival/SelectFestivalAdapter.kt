package com.example.festival

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SelectFestivalAdapter(private var festivals: List<Festival>):
    RecyclerView.Adapter<SelectFestivalAdapter.SelectFestivalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectFestivalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_festival_recyclerview, parent, false)
        return SelectFestivalViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectFestivalViewHolder, position: Int) {
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

    inner class SelectFestivalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.festival_title)

        init {
            itemView.setOnClickListener {
                val clickedFestival = festivals[adapterPosition]
                val festivalId = clickedFestival.festivalId // 클릭된 페스티벌의 Id를 가져옴
                val festivalTitle = clickedFestival.title

                val resultIntent = Intent()
                resultIntent.putExtra("festivalId", festivalId)
                resultIntent.putExtra("festivalTitle", festivalTitle)
                (itemView.context as Activity).setResult(Activity.RESULT_OK, resultIntent)
                (itemView.context as Activity).finish()
            }
        }

        fun bind(festivalList: Festival) {
            titleTextView.text = festivalList.title
        }

    }
}