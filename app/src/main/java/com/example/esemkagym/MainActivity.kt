package com.example.esemkagym

import android.os.Bundle
import android.view.Gravity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.esemkagym.databinding.ActivityMainBinding
import com.example.esemkagym.ui.fragment.DailyCheckInCodeFragment
import com.example.esemkagym.ui.fragment.ManageMemberFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    val label = "daily"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        showFragment(DailyCheckInCodeFragment())

        binding.close.setOnClickListener {
            binding.sideBar.closeDrawer(GravityCompat.START)
        }

        binding.menuCode.setOnClickListener {
            showFragment(DailyCheckInCodeFragment())
        }

        binding.menuMember.setOnClickListener {
            binding.sideBar.closeDrawer(GravityCompat.START)
            showFragment(ManageMemberFragment())
        }
    }

    fun resume() {

    }


    fun openDraw() {
        binding.sideBar.openDrawer(GravityCompat.START)
    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }
}