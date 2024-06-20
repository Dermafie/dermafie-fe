package com.example.storyapp.data.retrofit

import com.example.dermafie.data.response.FileUploadResponse
import com.example.dermafie.data.response.HistoryResponse
import com.example.dermafie.data.response.Login2Response
import com.example.dermafie.data.response.ProfileResponse
import com.example.dermafie.data.response.Register2Response
import com.example.dermafie.data.response.UploadProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("users/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Register2Response>

    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<Login2Response>

    @GET("users/profile")
    fun getUserProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    @Multipart
    @PUT("users/profile")
    fun uploadProfilePicture(
        @Header("Authorization") token: String,
        @Part profilePicture: MultipartBody.Part
    ): Call<UploadProfileResponse>

    @Multipart
    @POST("analyze")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): FileUploadResponse

    @GET("analyze/history")
    fun getHistory(
        @Header("Authorization") token: String
    ): Call<HistoryResponse>

}