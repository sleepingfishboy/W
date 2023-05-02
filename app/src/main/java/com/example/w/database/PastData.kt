package com.example.w.database

/**
 *作者：sleepingfishboy
 *时间：2023/5/2

 */
data class PastData(
    val date: String,
    val stories: List<Story>
) {
    data class Story(
        val hint: String,
        val images: List<String>,
        val title: String,
        val url: String
    )
}