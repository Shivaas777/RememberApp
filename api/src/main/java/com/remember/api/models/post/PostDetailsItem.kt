package com.remember.api.models.post

data class PostDetailsItem(
    val born_date: String,
    val comments: ArrayList<Comment>,
    val days_to_go: String,
    val death_date: String,
    val post_content: String,
    val post_created_by: String,
    val post_head: PostHead,
    val post_id: Int,
    val post_time_diff: String,
    val post_time_value: String,
    val post_title: String,
    val relation: String,
    val share: Int,
    val comment_count:Int,
    val event_date: String,
    val views: Int,
    val created_at:String
)