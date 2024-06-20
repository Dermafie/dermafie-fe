package com.example.dermafie.ui.history

import com.example.dermafie.data.response.HistoryResponse
import com.example.storyapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryRepository {

    fun getHistory(token: String, callback: (Result<HistoryResponse>) -> Unit) {
        val call = ApiConfig.getApiService().getHistory("Bearer $token")
        call.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()!!))
                } else {
                    callback(Result.failure(Exception(response.message())))
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}
