package com.example.firebasetaskappkaushal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.firebasetaskappkaushal.R
import com.example.firebasetaskappkaushal.databinding.DialogDataShowBinding

class DataDialog : DialogFragment() {

    private var _binding: DialogDataShowBinding? = null
    private val binding get() = _binding!!
    private val args: DataDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = DialogDataShowBinding.inflate(inflater, container, false)
        val userInfo = args.userInfo
        binding.apply {
            tvFirstName.text = getString(R.string.first_name, userInfo.firstName)
            tvLastName.text = getString(R.string.last_name, userInfo.lastName)
            tvAge.text = getString(R.string.age, userInfo.age)
            tvGender.text = getString(R.string.gender, userInfo.gender)
            tvClassroom.text = getString(R.string.classroom, userInfo.classRoom)
            Glide.with(binding.ivUser)
                .load(userInfo.image)
                .placeholder(R.drawable.ic_user)
                .into(binding.ivUser)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}