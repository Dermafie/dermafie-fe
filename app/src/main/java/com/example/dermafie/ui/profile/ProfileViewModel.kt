package com.example.dermafie.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dermafie.data.response.DataProfile
import com.example.dermafie.data.response.ProfileResponse
import com.example.storyapp.data.UserSession
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class ProfileViewModel : ViewModel() {
//
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is dashboard Fragment"
//    }
//    val text: LiveData<String> = _text
//
//
//}
class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _profileData = MutableLiveData<DataProfile>()
    val profileData: LiveData<DataProfile> = _profileData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchUserProfile(token: String) {
        Log.d(TAG, "Fetching user profile with token: $token")
        val client = ApiConfig.getApiService().getUserProfile("$token")
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                Log.d(TAG, "onResponse called")
                if (response.isSuccessful) {
                    val userProfileResponse = response.body()
                    Log.d(TAG, "Response: $userProfileResponse")
                    if (userProfileResponse != null) {
                        val userProfile = userProfileResponse.data
                        _profileData.value = userProfile
                        Log.d(TAG, "onSuccess: $userProfile")
                    } else {
                        Log.e(TAG, "onFailure: Invalid response")
                        _errorMessage.value = "Invalid response"
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Response failure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}", t)
                _errorMessage.value = "Network failure: ${t.message}"
            }
        })
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}
//class ProfileViewModel(private val apiService: ApiService, private val userSession: UserSession) : ViewModel() {
//
//    private val _profileData = MutableLiveData<DataProfile>()
//    val profileData: LiveData<DataProfile> = _profileData
//
//    private val _errorMessage = MutableLiveData<String>()
//    val errorMessage: LiveData<String> = _errorMessage
//
//    fun fetchUserProfile() {
//        viewModelScope.launch {
//            val token = "Bearer ${userSession.getToken().first()}"
//            val client = apiService.getUserProfile(token)
//            client.enqueue(object : Callback<ProfileResponse> {
//                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
//                    if (response.isSuccessful) {
//                        val userProfileResponse = response.body()
//                        if (userProfileResponse != null) {
//                            val userProfile = userProfileResponse.data
//                            _profileData.value = userProfile
//                            Log.e(TAG, "onSuccess: $userProfile")
//                        } else {
//                            Log.e(TAG, "onFailure: Invalid response or status is not 'berhasil'")
//                            _errorMessage.value = "Invalid response"
//                        }
//                    } else {
//                        Log.e(TAG, "onFailure1: ${response.message()}")
//                        _errorMessage.value = "Response failure: ${response.message()}"
//                    }
//                }
//
//                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
//                    Log.e(TAG, "onFailure2: ${t.message}", t)
//                    _errorMessage.value = "Network failure: ${t.message}"
//                }
//            })
//        }
//    }
//
//    companion object {
//        private const val TAG = "ProfileViewModel"
//    }
//}