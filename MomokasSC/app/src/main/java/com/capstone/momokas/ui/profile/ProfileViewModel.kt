package com.capstone.momokas.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.UserResponse
import com.capstone.momokas.data.repository.KendaraanRepository

class ProfileViewModel(private var repository: KendaraanRepository = KendaraanRepository()) : ViewModel() {

    fun getUserData(id: String) : LiveData<UserResponse> = repository.getDataUser(id)

}