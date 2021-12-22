package com.capstone.momokas.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.momokas.data.KendaraanDataSource
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.SliderResponse
import com.capstone.momokas.data.remote.response.UserResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class KendaraanRepository : KendaraanDataSource {

    private val dbStorage = Firebase.database.getReference("Kendaraan")
    private val dbRealtime = FirebaseDatabase.getInstance().reference

    override fun getAllListKendaraan(): LiveData<List<KendaraanResponse>> {
        val listKendaraan = MutableLiveData<List<KendaraanResponse>>()
        dbStorage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: List<KendaraanResponse> = snapshot.children.map {
                    it.getValue(KendaraanResponse::class.java)!!
                }
                listKendaraan.postValue(list)
                Log.v("GET_ALL_ITEM", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GET_ALL_ITEM", "msg: ${error.message}")
            }

        })
        return listKendaraan
    }

    override fun getListMotor(): LiveData<List<KendaraanResponse>> {
        val listKendaraan = MutableLiveData<List<KendaraanResponse>>()
        dbStorage.orderByChild("jenis").equalTo("Motor").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: List<KendaraanResponse> = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(KendaraanResponse::class.java)!!
            }
                listKendaraan.postValue(list)
                Log.v("GET_MOTOR", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GET_MOTOR", "msg: ${error.message}")
            }

        })
        return listKendaraan
    }

    override fun getListMobil(): LiveData<List<KendaraanResponse>> {
        val listKendaraan = MutableLiveData<List<KendaraanResponse>>()
        dbStorage.orderByChild("jenis").equalTo("Mobil").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: List<KendaraanResponse> = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(KendaraanResponse::class.java)!!
                }
                listKendaraan.postValue(list)
                Log.v("GET_MOBIL", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GET_MOBIL", "msg: ${error.message}")
            }

        })
        return listKendaraan
    }

    override fun getDataUser(id: String): LiveData<UserResponse> {
        val dataUser = MutableLiveData<UserResponse>()
        dbRealtime.child("User").child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data: UserResponse? = snapshot.getValue(UserResponse::class.java)
                dataUser.postValue(data!!)
                Log.v("GET_USER", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GET_USER", "msg: ${error.message}")
            }

        })
        return dataUser
    }

    override fun getListKendaraanUser(id: String): LiveData<List<KendaraanResponse>> {
        val listKendaraan = MutableLiveData<List<KendaraanResponse>>()
        dbStorage.orderByChild("user_id").equalTo(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: List<KendaraanResponse> = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(KendaraanResponse::class.java)!!
                }
                listKendaraan.postValue(list)
                Log.v("GET_KENDARAAN_USER", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GET_KENDARAAN_USER", "msg: ${error.message}")
            }

        })
        return listKendaraan
    }


    override fun getListSlider(): LiveData<List<SliderResponse>> {
        val listSlider = MutableLiveData<List<SliderResponse>>()
        dbRealtime.child("Slider").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: List<SliderResponse> = snapshot.children.map {
                    it.getValue(SliderResponse::class.java)!!
                }
                listSlider.postValue(list)
                Log.v("GET_SLIDER", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GET_SLIDER", "msg: ${error.message}")
            }

        })
        return listSlider
    }


}