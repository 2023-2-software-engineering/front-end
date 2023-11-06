package com.example.festival

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class BoardFragment : Fragment() {
    private lateinit var boardAdapter: BoardAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.boardRecyclerView) // 리사이클러뷰 초기화

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        boardAdapter = BoardAdapter(emptyList()) // 초기에 빈 목록으로 어댑터 설정
        recyclerView.adapter = boardAdapter // 리사이클러뷰에 어댑터 설정

        loadBoardList()

        // 게시판 초가 버튼 클릭 시 게시판 추가 Activity 로 이동
        val addBoardButton = view.findViewById<ExtendedFloatingActionButton>(R.id.board_add_btn)
        addBoardButton.setOnClickListener {
            val intent = Intent(activity, AddBoardActivity::class.java)
            startActivity(intent)
        }
    }

    // 서버에서 게시판 리스트 불러오기
    private fun loadBoardList() {
        BoardManager.getBoardListData(
            onSuccess = { boardListResponse ->
                Log.d("my log", "${boardListResponse}")
                val board = boardListResponse.map { it }
                Log.d("my log", "${board}")
                boardAdapter.updateData(board)
            },
            onError = { throwable ->
                Log.e("서버 테스트", "오류3: $throwable")
            }
        )
    }

}