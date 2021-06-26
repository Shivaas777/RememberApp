package com.remember.api.models.store


import com.google.gson.annotations.SerializedName

data class PlaceOrderRequest(
    @SerializedName("billing_add")
    val billingAdd: Address,
    @SerializedName("delivery_add")
    val deliveryAdd: Address,
    @SerializedName("member_id")
    val memberId: Int,
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("user_id")
    val userId: String
)