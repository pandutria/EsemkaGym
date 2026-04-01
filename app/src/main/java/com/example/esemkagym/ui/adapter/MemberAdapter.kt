package com.example.esemkagym.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkagym.data.model.Attendance
import com.example.esemkagym.data.model.Member
import com.example.esemkagym.databinding.ItemAttendanceBinding
import com.example.esemkagym.databinding.ItemMemberBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MemberAdapter(
    private val list: MutableList<Member> = mutableListOf(),
    private val status: String
): RecyclerView.Adapter<MemberAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.apply {
            binding.tvName.text = item.name

            when(status) {
                "ACTIVE" -> {
                    binding.tvDate.text = "Member until ${LocalDate.parse(item.joinedMemberAt)}"
                    binding.btnResume.text = "Resume"
                }
                "INACTIVE" -> {
                    binding.tvDate.text = "Last membership at ${LocalDate.parse(item.membershipEnd)}"
                    binding.btnResume.text = "Resume"
                }
                "PENDING_APPROVAL" -> {
                    binding.tvDate.text = "Register at ${LocalDate.parse(item.registerAt)}"
                    binding.btnResume.text = "Confirm"
                }
            }
        }
    }
}