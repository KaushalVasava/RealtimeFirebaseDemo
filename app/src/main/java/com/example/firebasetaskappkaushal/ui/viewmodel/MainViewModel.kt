package com.example.firebasetaskappkaushal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasetaskappkaushal.R
import com.example.firebasetaskappkaushal.model.UserInfo
import com.example.firebasetaskappkaushal.util.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _operationState = MutableStateFlow<Int?>(null)
    val operationState: StateFlow<Int?> get() = _operationState.asStateFlow()

    private val _userDataFlow = MutableStateFlow<List<UserInfo>>(emptyList())
    val userDataFlow: StateFlow<List<UserInfo>> get() = _userDataFlow.asStateFlow()

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private val databaseRef: DatabaseReference by lazy {
        firebaseDatabase.getReference(AppConstants.DATABASE_NAME)
    }

    init {
        getUserData()
    }

    fun getUserData() {
        viewModelScope.launch {
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    _userDataFlow.value = emptyList()
                    _operationState.value = R.string.fetch_error
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = mutableListOf<UserInfo>()
                    snapshot.children.map {
                        val userInfo = it.getValue(UserInfo::class.java)
                        if (userInfo != null) {
                            users.add(userInfo)
                        }
                    }
                    _userDataFlow.value = users
                }
            })
        }
    }

    fun addUserData(userInfo: UserInfo) {
        viewModelScope.launch {
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    databaseRef.child(userInfo.id).setValue(userInfo)
                    _operationState.value = R.string.data_success_msg
                }

                override fun onCancelled(error: DatabaseError) {
                    _operationState.value = R.string.save_error
                }
            })
        }
    }
}