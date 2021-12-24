package com.capstone.momokas.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.repository.KendaraanRepository

class DetailViewModel(private var repository: KendaraanRepository = KendaraanRepository()) :
    ViewModel()  {

    fun getDetail(id: String): LiveData<KendaraanResponse> = repository.getDetailKendaraan(id)
}