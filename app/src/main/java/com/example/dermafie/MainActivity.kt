package com.example.dermafie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
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
import com.google.android.material.bottomnavigation.BottomNavigationView

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        applyThemePreference()
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
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_scan, R.id.navigation_history, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.purple_200)))
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_theme -> {
                switchTheme()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchTheme() {
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPreferences.edit().putBoolean("dark_mode", false).apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPreferences.edit().putBoolean("dark_mode", true).apply()
        }
    }

    private fun applyThemePreference() {
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }




}