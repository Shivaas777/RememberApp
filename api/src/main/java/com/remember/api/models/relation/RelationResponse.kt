package com.remember.api.models.relation


import com.google.gson.annotations.SerializedName

data class RelationResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val response: ArrayList<Relation>
)