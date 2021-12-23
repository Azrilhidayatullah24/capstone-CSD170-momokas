package com.capstone.momokas.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.momokas.data.remote.response.UserResponse
import com.capstone.momokas.data.repository.KendaraanRepository

class PostViewModel(private var repository: KendaraanRepository = KendaraanRepository()) :
    ViewModel() {

    fun getUserData(id: String): LiveData<UserResponse> = repository.getDataUser(id)
}