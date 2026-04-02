package com.example.esemkagym.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkagym.R
import com.example.esemkagym.data.model.Attendance
import com.example.esemkagym.databinding.ItemAttendanceLogBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AttendanceLogAdapter(
    private val list: MutableList<Attendance> = mutableListOf()
): RecyclerView.Adapter<AttendanceLogAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemAttendanceLogBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttendanceLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.apply {
            val checkinParse = LocalDateTime.parse(item.checkIn)
            val checkinFormat = checkinParse.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            binding.tvCheckIn.text = checkinFormat.toString()

            if (item.checkOut != "null") {
                val checkoutParse = LocalDateTime.parse(item.checkOut)
                val checkoutFormat = checkoutParse.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                binding.tvCheckOut.text = checkoutFormat.toString()
            } else {
                binding.tvCheckOut.text = "-"
            }

            binding.tvName.text = item.name
            if (item.gender?.lowercase() == "male") {
                binding.imgImage.setImageResource(R.drawable.male)
            } else {
                binding.imgImage.setImageResource(R.drawable.female)
            }
        }
    }
}