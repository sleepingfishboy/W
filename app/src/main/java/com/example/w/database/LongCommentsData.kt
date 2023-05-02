package com.example.w.database

/**
 *作者：sleepingfishboy
 *时间：2023/5/2

 */
data class LongCommentsData(
    val comments: List<Comment>
) {
    data class Comment(
        val author: String,
        val avatar: String,
        val content: String
    ) {
        data class ReplyTo(
            val author: String,
            val content: String,
            val id: Int,
            val status: Int
        )
    }
}