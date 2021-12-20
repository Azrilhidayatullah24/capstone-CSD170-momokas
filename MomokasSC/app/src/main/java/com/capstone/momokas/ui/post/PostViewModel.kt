package com.capstone.momokas.ui.post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.UserResponse
import com.capstone.momokas.data.repository.KendaraanRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PostViewModel(private val repository: KendaraanRepository) : ViewModel() {
    fun getUserData(id: String) : LiveData<UserResponse> = repository.getDataUser(id)
}