package com.capstone.momokas.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.momokas.data.KendaraanDataSource
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class KendaraanRepository() : KendaraanDataSource {

    private val db = Firebase.database.getReference("Kenderaan/Motor")

    override fun getAllListKendaraan(): LiveData<List<KendaraanResponse>> {
        val listKendaraan = MutableLiveData<List<KendaraanResponse>>()
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: List<KendaraanResponse> = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(KendaraanResponse::class.java)!!.copy(id = dataSnapshot.key!!)
                }
                listKendaraan.postValue(list)
                Log.e("SNAPSHOT", snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("get_data_error", "msg: ${error.message}")
            }

        })
        return listKendaraan
    }


}