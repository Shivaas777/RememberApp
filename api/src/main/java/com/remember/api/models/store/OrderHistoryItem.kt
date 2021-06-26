package com.remember.api.models.store


import com.google.gson.annotations.SerializedName

data class OrderHistoryItem(
    @SerializedName("delivery_by")
    val deliveryBy: String,
    @SerializedName("discount_diffrence")
    val discountDiffrence: Int,
    @SerializedName("discount_percent")
    val discountPercent: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("img_url")
    val imgUrl: String,
    @SerializedName("order_status")
    val orderStatus: Int,
    @SerializedName("product_dis_price")
    val productDisPrice: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_price")
    val productPrice: Int
)