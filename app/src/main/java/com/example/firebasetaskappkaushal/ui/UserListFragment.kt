package com.example.firebasetaskappkaushal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.firebasetaskappkaushal.databinding.FragmentUserListBinding
import com.example.firebasetaskappkaushal.model.UserInfo
import com.example.firebasetaskappkaushal.ui.adapter.ItemClickListener
import com.example.firebasetaskappkaushal.ui.adapter.UserInfoAdapter
import com.example.firebasetaskappkaushal.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserListFragment : Fragment(), ItemClickListener {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    private val userInfoAdapter: UserInfoAdapter by lazy { UserInfoAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.apply {
            setHasFixedSize(true)
            adapter = userInfoAdapter
        }
        setUserDataObserver()
        setOperationObserver()
    }

    private fun setOperationObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.operationState.collectLatest { errorMsgId: Int? ->
                    if (errorMsgId != null) {
                        val msgError = getString(errorMsgId)
                        Toast.makeText(requireContext(), msgError, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setUserDataObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userDataFlow.collectLatest {
                    binding.recyclerview.isVisible = it.isNotEmpty()
                    binding.progressBar.isVisible = it.isEmpty()
                    userInfoAdapter.submitList(it)
                }
            }
        }
    }

    override fun onItemClick(userInfo: UserInfo) {
        val action = UserListFragmentDirections.actionUserListFragmentToDataDialog(userInfo)
        findNavController().navigate(action)
    }
}