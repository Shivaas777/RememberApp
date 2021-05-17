package com.remember.api.models.members

data class GetSingleMemberResponse(
    val isSuccess: Boolean,
    val message: String,
    val response: Members
)
