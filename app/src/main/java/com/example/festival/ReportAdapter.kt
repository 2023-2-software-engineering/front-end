package com.example.festival

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ReportAdapter(private var reports: List<ReportData>) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.report_recyclerview, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]
        holder.bind(report)
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newReports: List<ReportData>) {
        reports = newReports
        notifyDataSetChanged()
    }

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.report_title)
        private val writerTextView: TextView = itemView.findViewById(R.id.report_writer)
        private val doneTextView: TextView = itemView.findViewById(R.id.report_done)
        private val yetTextView: TextView = itemView.findViewById(R.id.report_yet)
        private val createdTextView: TextView = itemView.findViewById(R.id.report_date)

        init {
            itemView.setOnClickListener {
                val clickedReport = reports[adapterPosition]
                val reportId = clickedReport.reportId  // 클릭된 신고 Id를 가져옴
                val intent = Intent(itemView.context, ReportDetailActivity::class.java)
                intent.putExtra("reportId", reportId)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(reportList: ReportData) {
            titleTextView.text = reportList.title
            writerTextView.text = reportList.nickname

            if (reportList.done == true) {  // 조치 완료 라면
                doneTextView.visibility = View.VISIBLE
                yetTextView.visibility = View.GONE
            } else {
                yetTextView.visibility = View.VISIBLE
                doneTextView.visibility = View.GONE
            }

            val parts = reportList.createdAt.split("T")

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