package com.example.dermafie.data.response

import com.google.gson.annotations.SerializedName

data class UploadProfileResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("error_code")
	val errorCode: Int,

	@field:SerializedName("message")
	val message: String
)

data class DataUpload(

	@field:SerializedName("profile_picture")
	val profilePicture: String
)
