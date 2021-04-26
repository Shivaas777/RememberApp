package com.remember.api.models.post

data class PostResponse(
    val isSuccess: Boolean,
    val message: String,
    val response: ArrayList<PostItem>
)