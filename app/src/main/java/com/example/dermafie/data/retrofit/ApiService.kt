package com.example.storyapp.data.retrofit

/*import com.example.dermafie.data.response.FileUploadResponse*/
import com.example.dermafie.data.response.FileUploadResponse
import com.example.dermafie.data.response.Login2Response
import com.example.dermafie.data.response.ProfileResponse
import com.example.dermafie.data.response.Register2Response
import com.example.dermafie.data.response.UploadProfileResponse
import com.example.storyapp.data.response.AddStoryResponse
import com.example.storyapp.data.response.DetailStoryResponse
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
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

    @GET("stories")
    fun getStories(
        @Header("Authorization") token : String
    ) : Call<StoryResponse>

    @GET("stories/{id}")
    fun getStoriesDetail(
        @Header("Authorization") token : String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : Call<AddStoryResponse>

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
    @POST("skin-cancer/predict")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): FileUploadResponse
}