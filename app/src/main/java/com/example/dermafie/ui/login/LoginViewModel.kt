package com.example.dermafie.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dermafie.data.response.Data
import com.example.dermafie.data.response.Login2Response
import com.example.storyapp.data.UserSession
import com.example.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserSession) : ViewModel() {
    private val _loginResult = MutableLiveData<Data>()
    val loginResult: LiveData<Data> = _loginResult

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> = _loginError

    fun login(email: String, password: String) {
        ApiConfig.getApiService().login(email, password).enqueue(object : Callback<Login2Response> {
            override fun onResponse(call: Call<Login2Response>, response: Response<Login2Response>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null ) {
                        _loginResult.value = loginResponse.data
                    } else {
                        _loginError.value = loginResponse?.message ?: "Login failed"
                    }
                } else {
                    _loginError.value = "Login failed"
                }
            }

            override fun onFailure(call: Call<Login2Response>, t: Throwable) {
                _loginError.value = "Network error: ${t.message}"
            }
        })
    }
    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }
//    fun saveSession(user: UserModel) {
//        viewModelScope.launch {
//            repository.saveSession(user)
//        }
//    }
}