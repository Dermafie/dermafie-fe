package com.example.dermafie.ui.profile

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.dermafie.R
import com.example.dermafie.data.reduceFileImage
import com.example.dermafie.data.response.UploadProfileResponse
import com.example.dermafie.data.uriToFile
import com.example.dermafie.databinding.FragmentProfileBinding
import com.example.dermafie.ui.ViewModelFactory
import com.example.dermafie.ui.welcome.WelcomeActivity
import com.example.storyapp.data.UserSession
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.prefs.Preferences



class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var userSession: UserSession
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(activity, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        userSession = UserSession.getInstance(requireContext().dataStore)

        binding.changePhoto.setOnClickListener { startGallery() }
        binding.buttonLogout.setOnClickListener {
            lifecycleScope.launch {
                userSession.saveToken("")
                val intent = Intent(requireContext(), WelcomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }
        binding.buttonSave.setOnClickListener {
            context?.cacheDir?.deleteRecursively()
            Toast.makeText(activity, "Save success", Toast.LENGTH_LONG).show()
        }

        profileViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            binding.tvUsername.text = profile.name ?: "Sambit"
            binding.tvEmail.text = profile.email ?: "sambit@gmail.com"
            val profilePicUrl = profile.profilepic ?: "mantra.jpeg"
            Glide.with(this)
                .load(profilePicUrl)
                .into(binding.photoProfile)
        }

        profileViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            val token = userSession.getToken().first()
            profileViewModel.fetchUserProfile("Bearer $token")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.photoProfile.setImageURI(it)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
            uploadProfilePicture(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun uploadProfilePicture(uri: Uri) {
        val context = requireContext()
        val imageFile = uriToFile(uri, context).reduceFileImage()
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("profile_picture", imageFile.name, requestImageFile)

        lifecycleScope.launch {
            val apiService = ApiConfig.getApiService()
            val token = userSession.getToken().first()
            apiService.uploadProfilePicture("Bearer $token", multipartBody).enqueue(object :
                Callback<UploadProfileResponse> {
                override fun onResponse(
                    call: Call<UploadProfileResponse>,
                    response: Response<UploadProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val successResponse = response.body()
                        showToast(successResponse?.message ?: "Profile picture uploaded successfully")
                        clearAppCache()
//                        refreshUserProfile()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, UploadProfileResponse::class.java)
                        showToast(errorResponse.message)
                        clearAppCache()
//                        refreshUserProfile()
                    }
                }

                override fun onFailure(call: Call<UploadProfileResponse>, t: Throwable) {
                    showToast("Error uploading profile picture: ${t.message}")
                    clearAppCache()
//                    refreshUserProfile()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun refreshUserProfile() {
        lifecycleScope.launch {
            val token = userSession.getToken().first()
            profileViewModel.fetchUserProfile("Bearer $token")
        }
    }


    private fun clearAppCache() {
        try {
            val cacheDir = requireContext().cacheDir
            cacheDir.deleteRecursively()
        } catch (e: Exception) {
            showToast("Failed to clear cache: ${e.message}")
        }
    }
}
