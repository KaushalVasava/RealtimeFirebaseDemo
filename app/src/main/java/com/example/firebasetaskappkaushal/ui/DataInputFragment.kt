package com.example.firebasetaskappkaushal.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.firebasetaskappkaushal.R
import com.example.firebasetaskappkaushal.databinding.FragmentDataInputBinding
import com.example.firebasetaskappkaushal.model.UserInfo
import com.example.firebasetaskappkaushal.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

class DataInputFragment : Fragment() {

    private var _binding: FragmentDataInputBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    private var selectedClassroom: String = "1"
    private var photoUrl: String? = null

    companion object {
        private const val PHOTO_BUNDLE_KEY = "photo_bundle_key"
        private const val CLASSROOM_BUNDLE_KEY = "classroom_bundle_key"
    }

    private val mediaPickerLauncher = registerForActivityResult(
        PickVisualMedia()
    ) { uri: Uri? ->
        photoUrl = if (uri != null) {
            Glide.with(binding.ivProfile)
                .load(uri)
                .placeholder(R.drawable.ic_user)
                .into(binding.ivProfile)
            uri.toString()
        } else ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDataInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val userInfo = getUserData()
            if (userInfo != null)
                viewModel.addUserData(userInfo)
        }
        binding.ivProfile.setOnClickListener {
            mediaPickerLauncher.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(PickVisualMedia.ImageOnly)
                    .build()
            )
        }
        setArrayAdapter()
        setOperationObserver()
    }

    private fun setOperationObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.operationState.collectLatest { msgId: Int? ->
                    if (msgId != null) {
                        val msg = getString(msgId)
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setArrayAdapter() {
        val adp3 = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.classroom, android.R.layout.simple_list_item_1
        )
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerClass.adapter = adp3
        binding.spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                position: Int,
                id: Long,
            ) {

                val selectedItem = binding.spinnerClass.selectedItem.toString()
                selectedClassroom = selectedItem
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                /* no-op */
            }
        }
    }

    private fun getUserData(): UserInfo? {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val age = binding.etAge.text.toString()
        val gender = if (binding.rbMale.isChecked) getString(R.string.male) else getString(R.string.female)
        return if (firstName.isEmpty()) {
            Toast.makeText(requireContext(),
                getString(R.string.please_enter_first_name), Toast.LENGTH_SHORT)
                .show()
            null
        } else if (lastName.isEmpty()) {
            Toast.makeText(requireContext(),
                getString(R.string.please_enter_lastname), Toast.LENGTH_SHORT)
                .show()
            null
        } else if (age.isEmpty()) {
            Toast.makeText(requireContext(),
                getString(R.string.please_enter_age), Toast.LENGTH_SHORT)
                .show()
            null
        } else {
            UserInfo(
                id = UUID.randomUUID().toString(),
                firstName = firstName,
                lastName = lastName,
                gender = gender,
                age = age.toInt(),
                classRoom = selectedClassroom,
                image = photoUrl
            )
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            photoUrl = it.getString(PHOTO_BUNDLE_KEY)
            selectedClassroom = it.getString(CLASSROOM_BUNDLE_KEY, "1")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putString(PHOTO_BUNDLE_KEY, photoUrl)
            putString(CLASSROOM_BUNDLE_KEY, selectedClassroom)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}