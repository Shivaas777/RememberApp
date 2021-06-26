package com.remember.api.models.store


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("street")
    val street: String,
    @SerializedName("zipcode")
    val zipcode: String,
    @SerializedName("name")
val name: String,
@SerializedName("email")
val email: String,
@SerializedName("mobile")
val mobile: String
)