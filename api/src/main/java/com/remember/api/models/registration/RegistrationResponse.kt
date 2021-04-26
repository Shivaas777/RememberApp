package com.remember.api.models.registration

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("isSuccess") var isSuccess : Boolean,
    @SerializedName("message") var message : String,
    @SerializedName("response") var response : Verification
)
