package com.remember.api.models.members

data class Members(
    val born_date: String,
    val death_date: String,
    val id: Int,
    val img_url: String,
    val location: String,
    val name: String,
    val relation: String,
    val bio :String
)