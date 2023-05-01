package com.example.w.database

data class Data(
    val date: String,
    val stories: List<Story>,
    val top_stories: List<TopStory>
) {
    data class Story(
        val ga_prefix: String,
        val hint: String,
        val id: Int,
        val image_hue: String,
        val images: List<String>,
        val title: String,
        val type: Int,
        val url: String
    )

    data class TopStory(
        val hint: String,
        val image: String,
        val title: String
    )
}