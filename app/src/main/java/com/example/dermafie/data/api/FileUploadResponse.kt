package com.example.dermafie.data.api

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("data")
    var data: DataResult = DataResult()
)

data class DataResult(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("result")
    var result: String? = null,
    @SerializedName("confidenceScore")
    var confidenceScore: Double? = null,
    @SerializedName("isAboveThreshold")
    var isAboveThreshold: Boolean? = null,
    @SerializedName("createdAt")
    var createdAt: String? = null
)

