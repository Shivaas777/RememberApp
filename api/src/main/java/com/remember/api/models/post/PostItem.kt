package com.remember.api.models.post

data class PostItem(
    val comments: Int,
    val post_content: String,
    val post_created_by: String,
    val post_head: PostHead,
    val post_id: Int,
    val post_time_diff: String,
    val post_time_value: String,
    val post_title: String,
    val born_date:String,
    val death_date :String,
    val relation :String,
    val days_to_go :String,
    val share: Int
)