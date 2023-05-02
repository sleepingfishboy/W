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
import com.example.w.database.LongCommentsData

/**
 *作者：sleepingfishboy
 *时间：2023/5/2

 */
class LongCommentsAdapter(private val comments: MutableList<LongCommentsData.Comment>) :
    RecyclerView.Adapter<LongCommentsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_comments, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comments[position])

    }

    override fun getItemCount(): Int {
        return comments.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val content: TextView = view.findViewById(R.id.tv_comments_item_content)
        private val author: TextView = view.findViewById(R.id.tv_comments_item_author)
        private val image: ImageView = view.findViewById(R.id.iv_comments_item_image)
        fun bind(listItem: LongCommentsData.Comment) {
            Glide.with(itemView)
                .load(listItem.avatar[0])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image)
            content.text = listItem.content
            author.text = listItem.author


        }

    }
}