package com.remember.api.models.relation


import com.google.gson.annotations.SerializedName

data class Relation(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)