package com.example.dermafie.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.dermafie.databinding.FragmentProfileBinding
import com.example.dermafie.ui.ViewModelFactory
import com.example.dermafie.ui.welcome.WelcomeActivity
import com.example.storyapp.data.UserSession
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.prefs.Preferences


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var userSession: UserSession

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val buttonLogout: Button = binding.buttonLogout
        buttonLogout.setOnClickListener {
            Log.d("DashboardFragment", "Logout button clicked")
            lifecycleScope.launch {
                UserSession.getInstance(requireContext().dataStore).saveToken("")
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = ApiConfig.getApiService()
        userSession = UserSession.getInstance(requireContext().dataStore)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        profileViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            binding.tvUsername.text = profile.name ?: "Sambit"
//            binding..text = profile.email
            // Load profile picture if available, otherwise set to "mantra.jpeg"
            val profilePicUrl = profile.profilepic
            Glide.with(this)
                .load(profilePicUrl)
                .into(binding.photoProfile)
        }

        profileViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        val token = runBlocking { userSession.getToken().first() }
        profileViewModel.fetchUserProfile("Bearer $token")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}