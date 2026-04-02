package com.example.esemkagym.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.esemkagym.R
import com.example.esemkagym.data.HttpHandler
import com.example.esemkagym.data.local.TokenManager
import com.example.esemkagym.data.model.Attendance
import com.example.esemkagym.data.model.BarChart
import com.example.esemkagym.databinding.FragmentReportBinding
import com.example.esemkagym.ui.adapter.AttendanceAdapter
import com.example.esemkagym.ui.adapter.AttendanceLogAdapter
import com.example.esemkagym.utils.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReportBinding.inflate(layoutInflater, container, false)
        showData()
        return binding.root
    }

    fun showData() {
        val list: MutableList<Attendance> = mutableListOf()
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "attendance",
                    token = TokenManager(requireContext()).get()
                )
            }

            if (result.code in 200..300) {
                val array = JSONArray(result.body)

                for (i in 0 until array.length()) {
                    val data = array.getJSONObject(i)
                    val user = data.getJSONObject("user")

                    list.add(
                        Attendance(
                            id = data.getInt("id"),
                            checkIn = data.getString("checkIn"),
                            checkOut = data.getString("checkOut"),
                            gender = user.getString("gender"),
                            name = user.getString("name"),
                        )
                    )
                }

                binding.rv.adapter = AttendanceLogAdapter(list)
                binding.total.text = list.count().toString()

                val male = list.filter { it.gender?.lowercase() == "male" }.count()
                val female = list.filter { it.gender?.lowercase() == "female" }.count()

                val listBarChart: MutableList<BarChart> = mutableListOf()
                listBarChart.add(
                    BarChart(
                        "Male",
                        male,
                        ContextCompat.getColor(requireContext(), R.color.primary)
                    )
                )
                listBarChart.add(
                    BarChart(
                        "Female",
                        female,
                        ContextCompat.getColor(requireContext(), R.color.secondary)
                    )
                )

                binding.barChart.setData(listBarChart)
                Log.d("CHART", "male=$male, female=$female")
            } else {
                Helper.toast(
                    requireContext(),
                    JSONObject(result.body).getString("message")
                )
            }
        }
    }
}