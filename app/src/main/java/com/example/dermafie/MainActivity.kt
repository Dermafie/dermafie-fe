package com.example.dermafie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dermafie.databinding.ActivityMainBinding
import com.example.dermafie.ui.ViewModelFactory
import com.example.dermafie.ui.welcome.WelcomeActivity
import com.example.storyapp.data.UserSession
import com.example.storyapp.data.retrofit.ApiConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserSession.getInstance(dataStore)
        val apiService = ApiConfig.getApiService()
        val navView: BottomNavigationView = binding.navView
        mainViewModel = ViewModelProvider(this, ViewModelFactory(apiService, pref))[MainViewModel::class.java]
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        mainViewModel.getToken().observe(
            this
        ) { token: String ->
            if (token.isEmpty()) {
                val i = Intent(this, WelcomeActivity::class.java)
                startActivity(i)
                finish()
            } else {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            }
        }

//        val logOutButton : Button = findViewById(R.id.buttonLogout)
//        logOutButton.setOnClickListener {
//            Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show()
//        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_scan, R.id.navigation_history, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}