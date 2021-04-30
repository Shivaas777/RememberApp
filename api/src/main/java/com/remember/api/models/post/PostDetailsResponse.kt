package com.remember.api.models.post

data class PostDetailsResponse(
    val isSuccess: Boolean,
    val message: String,
    val response: PostDetailsItem
)