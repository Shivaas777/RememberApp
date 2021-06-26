package com.remember.api.models.store


import com.google.gson.annotations.SerializedName

data class StoreItem(
    @SerializedName("delivery_in_days")
    val deliveryInDays: Int,
    @SerializedName("delivery_on")
    val deliveryOn: String,
    @SerializedName("discount_diffrence")
    val discountDiffrence: Int,
    @SerializedName("discount_percent")
    val discountPercent: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("img_url")
    val imgUrl: String?,
    @SerializedName("in_stock")
    val inStock: Int,
    @SerializedName("is_active")
    val isActive: Int,
    @SerializedName("is_delete")
    val isDelete: Int,
    @SerializedName("product_dis_price")
    val productDisPrice: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_price")
    val productPrice: Int,
    @SerializedName("product_list")
    val productDesc: String,

)