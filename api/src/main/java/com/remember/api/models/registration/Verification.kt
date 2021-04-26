package com.remember.api.models.registration

import com.google.gson.annotations.SerializedName

data class Verification(
  @SerializedName("verify_id") var verifyId : String,
  @SerializedName("verify_OTP") var verifyOTP : String
  ) {
}