package com.example.dermafie.ui.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dermafie.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the data passed from ScanFragment
        val result = intent.getStringExtra("result")
        val confidenceScore = intent.getDoubleExtra("confidenceScore", 0.0)

        // Display the result in a TextView
        binding.resultTextView.text = String.format("%s with %.2f%% confidence", result, confidenceScore)
    }
}
