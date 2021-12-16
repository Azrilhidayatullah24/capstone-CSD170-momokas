package com.capstone.momokas.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.UserResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RemoteDataSource {
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val getRef: DatabaseReference = rootRef.child("User")
    private val auth =  FirebaseAuth.getInstance().currentUser

    interface InsertDataKendaraanCallback{
        fun onResponse(dataKendaraan: KendaraanResponse)
    }

    interface GetDataKendaraanCallback{
        fun onResponse(dataKendaraan: List<KendaraanResponse>)
    }

    interface GetDataUserCallback{
        fun onResponse(dataUser: UserResponse?)
    }

    fun insertKendaraan(jenisKendaraan: String, data: KendaraanResponse) {
        rootRef.child(auth?.uid!!).child(jenisKendaraan).setValue(data)
    }

    fun getUserData(userId: String, callback: GetDataUserCallback) {
        getRef.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataUser : UserResponse? = snapshot.getValue(UserResponse::class.java)
                callback.onResponse(dataUser)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DATA_USER", error.details + " " + error.message)
            }

        })
    }

    companion object{
        const val EXTRA_USER = "extra_user"
    }
}