package com.remember.api.models.registration

data class VerificationResponse(
    val isSuccess: Boolean,
    val message: String,
    val response: User
)