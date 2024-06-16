package com.example.dermafie.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dermafie.data.response.Register2Response
import com.example.dermafie.databinding.ActivityRegisterBinding
import com.example.dermafie.ui.login.LoginActivity
import com.example.storyapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            register()
        }
    }

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> = _registrationResult

    private fun register() {
        val client = ApiConfig.getApiService().register(
            binding.nameEditText.text.toString(),
            binding.emailEditText.text.toString(),
            binding.passwordEditText.text.toString()
        )
        client.enqueue(object : Callback<Register2Response> {
            override fun onResponse(call: Call<Register2Response>, response: Response<Register2Response>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        _registrationResult.value = RegistrationResult(success = true, message = registerResponse.message)
                        login() // Call goLogin function if registration is successful
                        Toast.makeText(this@RegisterActivity, "Register Success", Toast.LENGTH_LONG).show()
                    } else {
                        _registrationResult.value = RegistrationResult(success = false, message = registerResponse?.message ?: "Registration failed")
                    }
                } else {
                    _registrationResult.value = RegistrationResult(success = false, message = "Registration failed")
                }
            }

            override fun onFailure(call: Call<Register2Response>, t: Throwable) {
                _registrationResult.value = RegistrationResult(success = false, message = "Network error")
            }
        })
    }

    private fun login(){
        val i = Intent(this, LoginActivity::class.java)
        i.putExtra("email", binding.emailEditText.text.toString())
        i.putExtra("password", binding.passwordEditText.text.toString())
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        finish()
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

//        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val Register = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                Register
            )
            startDelay = 100
        }.start()
    }
}
data class RegistrationResult(
    val success: Boolean,
    val message: String
)