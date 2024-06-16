package com.example.dermafie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dermafie.MainViewModel
import com.example.dermafie.ui.login.LoginViewModel
import com.example.dermafie.ui.profile.ProfileViewModel
import com.example.storyapp.data.UserSession
import com.example.storyapp.data.retrofit.ApiService

class ViewModelFactory(private val apiService: ApiService, private val pref : UserSession) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
//            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
//                ProfileViewModel(apiService, pref) as T
//            }
//            modelClass.isAssignableFrom(TambahViewModel::class.java) -> {
//                TambahViewModel(pref) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}