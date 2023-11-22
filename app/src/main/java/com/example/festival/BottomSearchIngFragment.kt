package com.example.festival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSearchIngFragment : BottomSheetDialogFragment() {

    interface IngChangeListener {
        fun onIngChanged(ing: String)
    }

    private var ingChangeListener: IngChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_search_ing, container, false)

        // BottomSheet 높이/스타일 설정
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val layoutParams = bottomSheet?.layoutParams
            bottomSheet?.setBackgroundResource(R.drawable.bottom_sheet_rounded_corner)
            layoutParams?.height = 800
            bottomSheet?.layoutParams = layoutParams
        }

        val buttonAll = view.findViewById<TextView>(R.id.search_all)
        buttonAll.setOnClickListener {
            ingChangeListener?.onIngChanged("전체")
            dismiss() // 다이얼로그 닫기
        }

        val button1 = view.findViewById<TextView>(R.id.search_1)
        button1.setOnClickListener {
            ingChangeListener?.onIngChanged("진행예정")
            dismiss() // 다이얼로그 닫기
        }

        val button2 = view.findViewById<TextView>(R.id.search_2)
        button2.setOnClickListener {
            ingChangeListener?.onIngChanged("진행중")
            dismiss() // 다이얼로그 닫기
        }

        return view
    }

    fun setIngChangeListener(listener: BottomSearchIngFragment.IngChangeListener) {
        this.ingChangeListener = listener
    }
}