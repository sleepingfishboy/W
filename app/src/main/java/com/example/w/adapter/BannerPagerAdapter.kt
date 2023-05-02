package com.example.w.adapter

import android.content.Intent
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

/**
 *作者：sleepingfishboy
 *时间：2023/4/29

 */
class BannerPagerAdapter(private val bannerItems: List<Data.TopStory>) :
    RecyclerView.Adapter<BannerPagerAdapter.BannerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_banner,
            parent, false
        )
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerItems[position])
    }

    override fun getItemCount(): Int {
        return bannerItems.size
    }

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_banner_item_image)
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_banner_item_title)
        private val authorTextView: TextView = itemView.findViewById(R.id.tv_banner_item_hint)

        fun bind(bannerItem: Data.TopStory) {

            Glide.with(itemView)
                .load(bannerItem.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
            titleTextView.text = bannerItem.title
            authorTextView.text = bannerItem.hint

            // 为 ItemView 添加点击事件
            itemView.setOnClickListener {
                // 跳转到 WebView 显示对应链接
                val intent = Intent(itemView.context, StoryActivity::class.java)
                intent.putExtra("url", bannerItem.url)
                intent.putExtra("id", bannerItem.id)
                itemView.context. startActivity(intent)
            }

        }

    }
}

