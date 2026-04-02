package com.example.esemkagym.ui

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.esemkagym.R
import com.example.esemkagym.data.HttpHandler
import com.example.esemkagym.data.local.TokenManager
import com.example.esemkagym.databinding.ActivitySignUpBinding
import com.example.esemkagym.utils.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvSign.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
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

        binding.btn.setOnClickListener {
            if (binding.etEmail.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()
                || (!binding.rbMale.isChecked && !binding.rbFemale.isChecked)) {
                Helper.toast(this, "All fields must be filled")
                return@setOnClickListener
            }


        }
    }
}