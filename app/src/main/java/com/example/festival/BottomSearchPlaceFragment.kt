package com.example.festival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSearchPlaceFragment : BottomSheetDialogFragment() {

    interface PlaceChangeListener {
        fun onPlaceChanged(place: String)
    }

    private var placeChangeListener: PlaceChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_search_place, container, false)

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
        buttonAll.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("모든 지역")
            dismiss() // 다이얼로그 닫기
        }

        val button1 = view.findViewById<TextView>(R.id.search_1)
        button1.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("강남구")
            dismiss() // 다이얼로그 닫기
        }

        val button2 = view.findViewById<TextView>(R.id.search_2)
        button2.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("강동구")
            dismiss() // 다이얼로그 닫기
        }

        val button3 = view.findViewById<TextView>(R.id.search_3)
        button3.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("강서구")
            dismiss() // 다이얼로그 닫기
        }

        val button4 = view.findViewById<TextView>(R.id.search_4)
        button4.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("관악구")
            dismiss() // 다이얼로그 닫기
        }

        val button5 = view.findViewById<TextView>(R.id.search_5)
        button5.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("광진구")
            dismiss() // 다이얼로그 닫기
        }

        val button6 = view.findViewById<TextView>(R.id.search_6)
        button6.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("구로구")
            dismiss() // 다이얼로그 닫기
        }

        val button7 = view.findViewById<TextView>(R.id.search_7)
        button7.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("금천구")
            dismiss() // 다이얼로그 닫기
        }

        val button8 = view.findViewById<TextView>(R.id.search_8)
        button8.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("노원구")
            dismiss() // 다이얼로그 닫기
        }

        val button9 = view.findViewById<TextView>(R.id.search_9)
        button9.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("도봉구")
            dismiss() // 다이얼로그 닫기
        }

        val button10 = view.findViewById<TextView>(R.id.search_10)
        button10.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("동대문구")
            dismiss() // 다이얼로그 닫기
        }

        val button11 = view.findViewById<TextView>(R.id.search_11)
        button11.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("동작구")
            dismiss() // 다이얼로그 닫기
        }

        val button12 = view.findViewById<TextView>(R.id.search_12)
        button12.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("마포구")
            dismiss() // 다이얼로그 닫기
        }

        val button13 = view.findViewById<TextView>(R.id.search_13)
        button13.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("서대문구")
            dismiss() // 다이얼로그 닫기
        }

        val button14 = view.findViewById<TextView>(R.id.search_14)
        button14.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("서초구")
            dismiss() // 다이얼로그 닫기
        }

        val button15 = view.findViewById<TextView>(R.id.search_15)
        button15.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("성동구")
            dismiss() // 다이얼로그 닫기
        }

        val button16 = view.findViewById<TextView>(R.id.search_16)
        button16.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("성북구")
            dismiss() // 다이얼로그 닫기
        }

        val button17 = view.findViewById<TextView>(R.id.search_17)
        button17.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("송파구")
            dismiss() // 다이얼로그 닫기
        }

        val button18 = view.findViewById<TextView>(R.id.search_18)
        button18.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("양천구")
            dismiss() // 다이얼로그 닫기
        }

        val button19 = view.findViewById<TextView>(R.id.search_19)
        button19.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("영등포구")
            dismiss() // 다이얼로그 닫기
        }

        val button20 = view.findViewById<TextView>(R.id.search_20)
        button20.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("용산구")
            dismiss() // 다이얼로그 닫기
        }

        val button21 = view.findViewById<TextView>(R.id.search_21)
        button21.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("은평구")
            dismiss() // 다이얼로그 닫기
        }

        val button22 = view.findViewById<TextView>(R.id.search_22)
        button22.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("종로구")
            dismiss() // 다이얼로그 닫기
        }

        val button23 = view.findViewById<TextView>(R.id.search_23)
        button23.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("중구")
            dismiss() // 다이얼로그 닫기
        }

        val button24 = view.findViewById<TextView>(R.id.search_24)
        button24.setOnClickListener { //태그 검색 선택 시
            placeChangeListener?.onPlaceChanged("중랑구")
            dismiss() // 다이얼로그 닫기
        }

        return  view
    }

    fun setPlaceChangeListener(listener: PlaceChangeListener) {
        this.placeChangeListener = listener
    }
}