package com.example.dermafie.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: DataProfile,

	@field:SerializedName("error_code")
	val errorCode: Int,

	@field:SerializedName("message")
	val message: String
)

data class DataProfile(

	@field:SerializedName("profilepic")
	val profilepic: Any,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)
