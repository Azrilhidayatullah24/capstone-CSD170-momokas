package com.capstone.momokas.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.momokas.data.KendaraanDataSource
import com.capstone.momokas.data.remote.response.JenisKendaraan
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.UserResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class KendaraanRepository : KendaraanDataSource {

    private val dbStorage = Firebase.database.getReference("Kendaraan")
    private val dbRealtime = FirebaseDatabase.getInstance().getReference("User")

    override fun getAllListKendaraan(): LiveData<List<KendaraanResponse>> {
        val listKendaraan = MutableLiveData<List<KendaraanResponse>>()
        dbStorage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: List<KendaraanResponse> = snapshot.children.map {
                    it.getValue(KendaraanResponse::class.java)!!
                }
                listKendaraan.postValue(list)
                Log.e("GET_ALL_ITEM", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("GET_ALL_ITEM", "msg: ${error.message}")
            }

        })
        return listKendaraan
    }

    override fun getListMotor(): LiveData<List<KendaraanResponse>> {
        val listKendaraan = MutableLiveData<List<KendaraanResponse>>()
        dbStorage.orderByChild("Jenis").equalTo("Motor").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val type = ds.child("tipe").getValue(String::class.java)
                    Log.d("tipe", type.toString())
                }

//                val list: List<KendaraanResponse> = snapshot.children.map { dataSnapshot ->
//                    dataSnapshot.getValue(KendaraanResponse::class.java)!!
//            }
//                listKendaraan.postValue(list)
                Log.e("SNAPSHOT", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SNAPSHOT", "msg: ${error.message}")
            }

        })
        return listKendaraan
    }

    override fun getListMobil(): LiveData<List<KendaraanResponse>> {
        val listKendaraan = MutableLiveData<List<KendaraanResponse>>()
        dbStorage.equalTo("Mobil").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: List<KendaraanResponse> = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(KendaraanResponse::class.java)!!
                }
                listKendaraan.postValue(list)
                Log.e("SNAPSHOT", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SNAPSHOT", "msg: ${error.message}")
            }

        })
        return listKendaraan
    }

    override fun getDataUser(id: String): LiveData<UserResponse> {
        val dataUser = MutableLiveData<UserResponse>()
        dbRealtime.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data: UserResponse? = snapshot.getValue(UserResponse::class.java)
                dataUser.postValue(data!!)
                Log.e("SNAPSHOT", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SNAPSHOT", "msg: ${error.message}")
            }

        })
        return dataUser
    }


}