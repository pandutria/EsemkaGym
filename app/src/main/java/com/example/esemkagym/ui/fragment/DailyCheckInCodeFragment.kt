package com.example.esemkagym.ui.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import com.example.esemkagym.MainActivity
import com.example.esemkagym.R
import com.example.esemkagym.data.HttpHandler
import com.example.esemkagym.data.local.StatusManager
import com.example.esemkagym.data.local.TokenManager
import com.example.esemkagym.databinding.ActivityMainBinding
import com.example.esemkagym.databinding.FragmentDailyCheckInCodeBinding
import com.example.esemkagym.utils.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyCheckInCodeFragment : Fragment() {
    private var _binding: FragmentDailyCheckInCodeBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDailyCheckInCodeBinding.inflate(layoutInflater, container, false)

        binding.tvDate.text = LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
        showCode()

        binding.tvMenu.setOnClickListener {
            (requireContext() as MainActivity).openDraw()
        }

        return binding.root
    }

    fun showCode() {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "attendance/checkin/code",
                    "GET",
                    token = TokenManager(requireContext()).get()
                )
            }

            if (result.code in 200..300) {
                binding.tvCode.text = JSONObject(result.body).getString("code")
            }
        }
    }

}