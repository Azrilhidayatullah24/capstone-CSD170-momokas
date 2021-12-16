package com.capstone.momokas.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.Response
import com.capstone.momokas.data.repository.KendaraanRepository

class HomeViewModel(private var repository: KendaraanRepository = KendaraanRepository()) : ViewModel() {

    fun getListSemuKendaraan() : LiveData<List<KendaraanResponse>> = repository.getAllListKendaraan()
}