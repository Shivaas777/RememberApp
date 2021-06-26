package com.remember.api.models.registration

data class User(
    var email: String,
    val id: String,
    val mobile: String,
    var name: String,
    var dob: String,
    var gender: Int,
    var address: String,
    var street: String,
    var city: String,
    var state: String,
    var country: String,
    var zipcode: String,
    var profile_image: String,
)