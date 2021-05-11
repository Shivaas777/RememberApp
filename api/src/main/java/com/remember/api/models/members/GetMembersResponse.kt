package com.remember.api.models.members

data class GetMembersResponse(
    val isSuccess: Boolean,
    val message: String,
    val response: ArrayList<Members>
)