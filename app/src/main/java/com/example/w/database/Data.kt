package com.example.w.database

data class Data(
    val date: String,
    val stories: List<Story>,
    val top_stories: List<TopStory>
) {
    data class Story(
        val hint: String,
        val id: Int,
        val images: List<String>,
        val title: String,
        val url: String
    )

    data class TopStory(
        val hint: String,
        val id: Int,
        val image: String,
        val title: String,
        val url: String
    )
}