package com.remember.api.models.members

data class UploadImageResponse(
    val isSuccess: Boolean,
    val message: String,
    val response: String
) {
}