package com.example.esemkagym.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.esemkagym.R
import com.example.esemkagym.data.HttpHandler
import com.example.esemkagym.data.local.TokenManager
import com.example.esemkagym.data.model.Member
import com.example.esemkagym.databinding.FragmentManageMemberBinding
import com.example.esemkagym.ui.adapter.MemberAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class ManageMemberFragment : Fragment() {
    private var _binding: FragmentManageMemberBinding? = null
    private val binding get() = _binding!!
    var status = "ACTIVE"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentManageMemberBinding.inflate(layoutInflater, container, false)

        binding.menuActive.performClick()
        binding.menuActive.setOnClickListener {
            binding.menuActive.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_member_selected)
            binding.menuPast.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_member_unselected)
            binding.menuPending.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_member_unselected)
            status = "ACTIVE"
            checkPostion(status)
        }

        binding.menuPast.setOnClickListener {
            binding.menuActive.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_member_unselected)
            binding.menuPast.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_member_selected)
            binding.menuPending.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_member_unselected)
            status = "INACTIVE"
            checkPostion(status)
        }

        binding.menuPending.setOnClickListener {
            binding.menuActive.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_member_unselected)
            binding.menuPast.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_member_unselected)
            binding.menuPending.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_member_selected)
            status = "PENDING_APPROVAL"
            checkPostion(status)
        }

        binding.etSearch.addTextChangedListener {
            checkPostion(status)
        }

        return binding.root
    }

    fun checkPostion(status: String) {
        val list: MutableList<Member> = mutableListOf()
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                if (binding.etSearch.text.toString().isNotEmpty()) {
                    HttpHandler().request(
                        "member?name=${binding.etSearch.text}&status=$status",
                        token = TokenManager(requireContext()).get()
                    )
                } else {
                    HttpHandler().request(
                        "member?status=$status",
                        token = TokenManager(requireContext()).get()
                    )
                }
            }

            if (result.code in 200..300) {
                val array = JSONArray(result.body)

                for (i in 0 until array.length()) {
                    val data = array.getJSONObject(i)

                    list.add(
                        Member(
                            id = data.getInt("id"),
                            name = data.getString("name"),
                            registerAt = data.getString("registerAt"),
                            membershipEnd = data.getString("membershipEnd"),
                            joinedMemberAt = data.getString("joinedMemberAt")
                        )
                    )
                }

                binding.rv.adapter = MemberAdapter(list, status)
            }
        }
    }
}