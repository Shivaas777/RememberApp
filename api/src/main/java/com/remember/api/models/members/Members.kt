package com.remember.api.models.members

data class Members(
    val born_date: String,
    val death_date: String,
    val born_place:String,
    val death_place:String,
    val resting_in:String,
    val resting_in_lat:String,
    val resting_in_lon:String,
    val qr_code_url:String,
    val cover_pic:String,
    val id: Int,
    val img_url: String,
    val location: String,
    val name: String,
    val relation: String,
    val bio :String,
    val created_by :String,
    val created_by_name :String
)