package com.remember.api.models.post

data class ApiResponse(
    val isSuccess: Boolean,
    val message: String,
    val response: Response
)