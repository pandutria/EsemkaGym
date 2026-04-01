package com.example.esemkagym.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkagym.data.model.Attendance
import com.example.esemkagym.databinding.ItemAttendanceBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AttendanceAdapter(
    private val list: MutableList<Attendance> = mutableListOf()
): RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemAttendanceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        }
    }
}