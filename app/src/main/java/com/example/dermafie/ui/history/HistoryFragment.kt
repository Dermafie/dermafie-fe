package com.example.dermafie.ui.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dermafie.data.response.DataItem
import com.example.dermafie.dataStore
import com.example.dermafie.databinding.FragmentHistoryBinding
import com.example.dermafie.ui.HistoryAdapter
import com.example.storyapp.data.UserSession
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var userSession: UserSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the token from SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        userSession = UserSession.getInstance(requireContext().dataStore)

        binding.rvHistory.layoutManager = LinearLayoutManager(context)

        historyViewModel.historyData.observe(viewLifecycleOwner) { historyResponse ->
            setupRecyclerView(historyResponse)
        }

        historyViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            val token = userSession.getToken().first()
            historyViewModel.fetchUserHistory("Bearer $token")
        }
    }

    private fun setupRecyclerView(historyList: List<DataItem>) {
        historyAdapter = HistoryAdapter(historyList)
        binding.rvHistory.adapter = historyAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
