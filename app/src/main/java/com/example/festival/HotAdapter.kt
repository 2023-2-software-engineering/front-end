package com.example.festival

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HotAdapter(private var festivals: List<Festival>): RecyclerView.Adapter<HotAdapter.HotViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hot_recyclerview, parent, false)
        return HotViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
        val festival = festivals[position]
        holder.bind(festival, position + 1)
    }

    override fun getItemCount(): Int {
        return festivals.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newFestivals: List<Festival>) {
        festivals = newFestivals
        notifyDataSetChanged()
    }

    inner class HotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val likeTextView: TextView = itemView.findViewById(R.id.like_count)
        private val ranking: TextView = itemView.findViewById(R.id.ranking)

        init {
            itemView.setOnClickListener {
                val clickedFestival = festivals[adapterPosition]
                val festivalId = clickedFestival.festivalId // 클릭된 페스티벌의 Id를 가져옴
                val intent = Intent(itemView.context, FestivalDetailActivity::class.java)
                intent.putExtra("festivalId", festivalId)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(festivalList: Festival, position: Int) {
            titleTextView.text = festivalList.title
            ranking.text = position.toString() // 순서 매기기

            FestivalManager.getFestivalLike(
                festivalList.festivalId,
                onSuccess = { festivalLike ->
                    likeTextView.text = "$festivalLike"
                },
                onError = { throwable ->
                    Log.e("서버 테스트3", "오류: $throwable")
                }
            )
        }

    }
}