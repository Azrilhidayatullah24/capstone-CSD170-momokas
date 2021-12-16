package com.capstone.momokas.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.Response
import com.capstone.momokas.data.remote.response.UserResponse

interface KendaraanDataSource {
    fun getAllListKendaraan() : LiveData<List<KendaraanResponse>>
}