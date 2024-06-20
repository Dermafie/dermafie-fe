package com.example.dermafie.ui.history

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.dermafie.data.response.HistoryResponse
import com.example.dermafie.data.response.DataItem
import com.example.storyapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HistoryViewModel : ViewModel() {

    private val _historyData = MutableLiveData<List<DataItem>>()
    val historyData: LiveData<List<DataItem>> = _historyData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchUserHistory(token: String) {
        Log.d(TAG, "Fetching user history with token: $token")
        val client = ApiConfig.getApiService().getHistory(token)
        client.enqueue(object : Callback<HistoryResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                Log.d(TAG, "onResponse called")
                if (response.isSuccessful) {
                    val userHistoryResponse = response.body()
                    Log.d(TAG, "Response: $userHistoryResponse")
                    if (userHistoryResponse != null) {
                        val userHistory = userHistoryResponse.data.map {
                            it.copy(scanDate = formatScanDate(it.scanDate))
                        }
                        _historyData.value = userHistory.sortedByDescending { it.scanDate }
                        Log.d(TAG, "onSuccess: $userHistory")
                    } else {
                        Log.e(TAG, "onFailure: Invalid response")
                        _errorMessage.value = "Invalid response"
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Response failure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}", t)
                _errorMessage.value = "Network failure: ${t.message}"
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatScanDate(scanDate: String): String {
        val originalDate = ZonedDateTime.parse(scanDate, DateTimeFormatter.ISO_DATE_TIME)
        return originalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    companion object {
        private const val TAG = "HistoryViewModel"
    }
}
