package com.example.festival

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MultiImageAdapter(private val mData: ArrayList<Uri>, private val mContext: Context) :
    RecyclerView.Adapter<MultiImageAdapter.ViewHolder>() {

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.img)
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    // LayoutInflater - XML에 정의된 Resource(자원) 들을 View의 형태로 반환.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiImageAdapter.ViewHolder {
        val context: Context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.multi_image_recyclerview, parent, false)
        return ViewHolder(view)
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    override fun onBindViewHolder(holder: MultiImageAdapter.ViewHolder, position: Int) {
        val imageUri: Uri = mData[position]
        Log.d("my log", ""+imageUri)
        Glide.with(mContext)
            .load(imageUri)
            .skipMemoryCache(true)  // 예외처리(필수x)
            .diskCacheStrategy(DiskCacheStrategy.NONE)  // 예외처리(필수x)
            .into(holder.image)
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    override fun getItemCount(): Int {
        return mData.size
    }
}