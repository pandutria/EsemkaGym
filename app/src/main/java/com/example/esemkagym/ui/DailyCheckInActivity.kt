package com.example.esemkagym.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.esemkagym.R
import com.example.esemkagym.data.HttpHandler
import com.example.esemkagym.data.local.StatusManager
import com.example.esemkagym.data.local.TokenManager
import com.example.esemkagym.data.model.Attendance
import com.example.esemkagym.databinding.ActivityDailyCheckInBinding
import com.example.esemkagym.ui.adapter.AttendanceAdapter
import com.example.esemkagym.utils.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyCheckInActivity : AppCompatActivity() {
    private var _binding: ActivityDailyCheckInBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityDailyCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (LocalDate.now() > LocalDate.parse(
                intent.getStringExtra("membershipEnd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
        ) {
            Helper.toast(
                this, "Your membership has ended at ${
                    LocalDate.parse(
                        intent.getStringExtra("membershipEnd"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                }, resume your membership when visiting the gym next time"
            )
            binding.etCode.isEnabled = false
            binding.btn.isEnabled = false
        } else {
            binding.etCode.isEnabled = true
            binding.btn.isEnabled = true
        }

        binding.tvSignOut.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        showData()

        binding.btn.setOnClickListener {
            if (StatusManager(this).get() == false) {
                if (binding.etCode.text.toString().isEmpty()) {
                    Helper.toast(this, "A field must be filled")
                    return@setOnClickListener
                }
                checkIn()
            } else {
                checkOut()
            }
        }

        if (StatusManager(this).get() == false) {
            binding.etCode.isEnabled = true
            binding.btn.text = "Check In"
        } else {
            binding.btn.text = "Check Out"
            binding.etCode.isEnabled = false
        }
    }

    fun showData() {
        val list: MutableList<Attendance> = mutableListOf()
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "attendance",
                    token = TokenManager(this@DailyCheckInActivity).get()
                )
            }

            if (result.code in 200..300) {
                val array = JSONArray(result.body)

                for (i in 0 until array.length()) {
                    val data = array.getJSONObject(i)

                    list.add(
                        Attendance(
                            checkIn = data.getString("checkIn"),
                            checkOut = data.getString("checkOut")
                        )
                    )
                }

                binding.rv.adapter = AttendanceAdapter(list)
            } else {
                Helper.toast(
                    this@DailyCheckInActivity,
                    JSONObject(result.body).getString("message")
                )
            }
        }
    }

    fun checkOut() {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "attendance/checkout",
                    "POST",
                    token = TokenManager(this@DailyCheckInActivity).get()
                )
            }

            if (result.code in 200..300) {
                showData()
                binding.btn.text = "Check In"
                binding.etCode.isEnabled = true
                StatusManager(this@DailyCheckInActivity).save(false)
            } else {
                Helper.toast(
                    this@DailyCheckInActivity,
                    JSONObject(result.body).getString("message")
                )
            }
        }
    }

    fun checkIn() {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "attendance/checkin/${binding.etCode.text}",
                    "POST",
                    token = TokenManager(this@DailyCheckInActivity).get()
                )
            }

            if (result.code in 200..300) {
                binding.etCode.text.clear()
                showData()
                binding.btn.text = "Check Out"
                binding.etCode.isEnabled = false
                StatusManager(this@DailyCheckInActivity).save(true)
            } else {
                Helper.toast(
                    this@DailyCheckInActivity,
                    JSONObject(result.body).getString("message")
                )
            }
        }
    }
}