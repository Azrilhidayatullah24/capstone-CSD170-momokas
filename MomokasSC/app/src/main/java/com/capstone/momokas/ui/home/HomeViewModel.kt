package com.capstone.momokas.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.SliderResponse
import com.capstone.momokas.data.repository.KendaraanRepository

class HomeViewModel(private var repository: KendaraanRepository = KendaraanRepository()) :
    ViewModel() {

    fun getListAllKendaraan(): LiveData<List<KendaraanResponse>> = repository.getAllListKendaraan()

    fun getListMotor(): LiveData<List<KendaraanResponse>> = repository.getListMotor()

    fun getListMobil(): LiveData<List<KendaraanResponse>> = repository.getListMobil()

    fun getSlider(): LiveData<List<SliderResponse>> = repository.getListSlider()
}