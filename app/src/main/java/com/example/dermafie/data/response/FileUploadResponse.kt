package com.example.dermafie.data.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("data")
    var data: DataResult = DataResult(),
    @SerializedName("error_code")
    var errorCode: Int? = null
)

data class DataResult(
    @SerializedName("prediction")
    var prediction: String? = null,
    @SerializedName("probability")
    var probability: Double? = null,
    @SerializedName("imageURL")
    var imageURL: String? = null,
    @SerializedName("disease")
    var disease: Disease? = Disease()
)

data class Disease(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("effects")
    var effects: String? = null,
    @SerializedName("solution")
    var solution: String? = null
)