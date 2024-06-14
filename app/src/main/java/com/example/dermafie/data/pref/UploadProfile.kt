package com.example.dermafie.data.pref

data class UploadProfile(
    val message: String,
    val data: ProfilePictureData?,
    val error_code: Int
)
data class ProfilePictureData(
    val profile_picture: String
)

