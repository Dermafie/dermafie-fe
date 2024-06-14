package com.example.dermafie.data.response

import com.google.gson.annotations.SerializedName

data class Register2Response(

	@field:SerializedName("error_code")
	val errorCode: Int,

	@field:SerializedName("message")
	val message: String
)
