package com.example.dermafie.data.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("error_code")
	val errorCode: Int,

	@field:SerializedName("message")
	val message: String
)

data class DiseaseHistory(

	@field:SerializedName("effects")
	val effects: String,

	@field:SerializedName("solution")
	val solution: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String
)

data class DataItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("disease")
	val disease: Disease,

	@field:SerializedName("imageURL")
	val imageURL: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("scanResult")
	val scanResult: String,

	@field:SerializedName("scanDate")
	val scanDate: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
