package com.remember.api.models.post

data class Response(
    val born_date: String,
    val comments: List<Comment>,
    val days_to_go: String,
    val death_date: String,
    val post_content: String,
    val post_created_by: String,
    val post_head: PostHeadX,
    val post_id: Int,
    val post_time_diff: String,
    val post_time_value: String,
    val post_title: String,
    val relation: String,
    val share: Int,
    val views: Int
)