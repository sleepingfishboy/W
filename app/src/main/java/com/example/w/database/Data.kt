package com.example.w.database

data class Data(
    val date: String,
    val stories: List<Story>,
    val top_stories: List<TopStory>
)