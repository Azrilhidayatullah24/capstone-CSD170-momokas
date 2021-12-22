package com.capstone.momokas.data

import androidx.lifecycle.LiveData
import com.capstone.momokas.data.remote.response.JenisKendaraan
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.SliderResponse
import com.capstone.momokas.data.remote.response.UserResponse

interface KendaraanDataSource {
    fun getAllListKendaraan() : LiveData<List<KendaraanResponse>>
    fun getListMotor(): LiveData<List<KendaraanResponse>>
    fun getListMobil(): LiveData<List<KendaraanResponse>>
    fun getDataUser(id: String) : LiveData<UserResponse>
    fun getListSlider() : LiveData<List<SliderResponse>>
    fun getListKendaraanUser(id: String): LiveData<List<KendaraanResponse>>
}