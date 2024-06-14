package com.example.dermafie.data.response

import com.google.gson.annotations.SerializedName

data class Login2Response(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("error_code")
	val errorCode: Int,

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("user")
	val user: String,

	@field:SerializedName("token")
	val token: String
)
