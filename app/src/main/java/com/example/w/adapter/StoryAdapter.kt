package com.example.w.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.w.R
import com.example.w.activity.StoryActivity
import com.example.w.database.Data
import com.example.w.database.PastData


/**
 *作者：sleepingfishboy
 *时间：2023/5/1

 */
class StoryAdapter(private val stories: MutableList<Data.Story>) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_daily, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stories[position])

    }

    override fun getItemCount(): Int {
        return stories.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tv_title)
        private val hint: TextView = view.findViewById(R.id.tv_hint)
        private val image: ImageView = view.findViewById(R.id.iv_image)
        fun bind(listItem: Data.Story) {
            Glide.with(itemView)
                .load(listItem.images[0])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image)
            title.text = listItem.title
            hint.text = listItem.hint

            // 为 ItemView 添加点击事件
            itemView.setOnClickListener {
                // 跳转到 WebView 显示对应链接
                val intent = Intent(itemView.context, StoryActivity::class.java).apply {
                    putExtra("url", listItem.url)
                    putExtra("id", listItem.id)
                }

                itemView.context.startActivity(intent)
            }
        }

    }

}
