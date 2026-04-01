package com.example.esemkagym.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.esemkagym.MainActivity
import com.example.esemkagym.R
import com.example.esemkagym.data.HttpHandler
import com.example.esemkagym.data.local.TokenManager
import com.example.esemkagym.databinding.ActivitySignInBinding
import com.example.esemkagym.utils.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SignInActivity : AppCompatActivity() {
    private var _binding: ActivitySignInBinding? = null
    private val binding get() = _binding!!

    var membershipEnd: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvSign.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        if (binding.etPassword.inputType == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)) {
            binding.imgEye.setImageResource(R.drawable.baseline_remove_red_eye_24)
            binding.etPassword.inputType = (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        } else {
            binding.imgEye.setImageResource(R.drawable.close_eye)
            binding.etPassword.inputType = (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
        }

        binding.imgEye.setOnClickListener {
            if (binding.etPassword.inputType == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)) {
                binding.imgEye.setImageResource(R.drawable.baseline_remove_red_eye_24)
                binding.etPassword.inputType = (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            } else {
                binding.imgEye.setImageResource(R.drawable.close_eye)
                binding.etPassword.inputType = (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
            }
        }

        // admin
        binding.etEmail.setText("admin@gmail.com")
        binding.etPassword.setText("admin")

        // active member
//        binding.etEmail.setText("ada.lovelace@gmail.com")
//        binding.etPassword.setText("ada.lovelace")

        // inactive member
//        binding.etEmail.setText("mark.hopper@gmail.com")
//        binding.etPassword.setText("mark.hopper")

        // pending approval
//        binding.etEmail.setText("margaret.hamilton@gmail.com")
//        binding.etPassword.setText("margaret.hamilton")

        binding.btn.setOnClickListener {
            if (binding.etEmail.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()) {
                Helper.toast(this, "All fields must be filled")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val rBody = JSONObject().apply {
                    put("email", binding.etEmail.text.toString())
                    put("password", binding.etPassword.text.toString())
                }

                val result = withContext(Dispatchers.IO) {
                    HttpHandler().request(
                        "login",
                        "POST",
                        rBody = rBody.toString()
                    )
                }

                if (result.code in 200..300) {
                    val data = JSONObject(result.body)
                    val user = data.getJSONObject("user")
                    TokenManager(this@SignInActivity).save(data.getString("token"))

                    if (!user.getBoolean("admin")) {
                        if (user.getString("joinedMemberAt") == "null") {
                            startActivity(Intent(this@SignInActivity, RegisteredActivity::class.java))
                            return@launch
                        }

                        val joinedDate = user.getString("joinedMemberAt")
                        val endDate = user.getString("membershipEnd")

                        val joinParse = LocalDate.parse(joinedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                        val endParse = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        membershipEnd = endParse

                        if (LocalDate.now() < joinParse) {
                            startActivity(Intent(this@SignInActivity, RegisteredActivity::class.java))
                            return@launch
                        }

                        val i = Intent(this@SignInActivity, DailyCheckInActivity::class.java)
                        i.putExtra("membershipEnd", endParse.toString())
                        startActivity(i)
                        finish()
                    } else {
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    }
                } else {
                    Helper.toast(this@SignInActivity, "Your data is not valid!")
                }
            }
        }
    }
}