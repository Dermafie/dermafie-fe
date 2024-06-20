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

        // Retrieve the data passed via the intent
        val resultMessage = intent.getStringExtra("resultMessage")
        val diseaseDescription = intent.getStringExtra("diseaseDescription")
        val diseaseEffects = intent.getStringExtra("diseaseEffects")
        val diseaseSolution = intent.getStringExtra("diseaseSolution")

        // Set the text for each TextView
        binding.resultTextView.text = "Prediction: $resultMessage"
        binding.diseaseDescriptionTextView.text = "Description: $diseaseDescription"
        binding.diseaseEffectsTextView.text = "Effects: $diseaseEffects"
        binding.diseaseSolutionTextView.text = "Solution: $diseaseSolution"
    }
}
