package com.remember.api.models.store


import com.google.gson.annotations.SerializedName

data class OrderHistoryResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val response: ArrayList<OrderHistoryItem>
)