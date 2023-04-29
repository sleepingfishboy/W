package com.example.w

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.*

/**
 *作者：sleepingfishboy
 *时间：2023/4/29

 */
class BannerPagerAdapter(private val bannerList: List<Int>) :
    RecyclerView.Adapter<BannerPagerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_banner_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView)
            .load(bannerList[position])
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

}