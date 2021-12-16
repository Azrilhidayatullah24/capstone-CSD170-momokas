package com.capstone.momokas.data.remote.response

data class UserResponse(
    var id: String? = null,
    var username: String? = null,
    var nama: String? = null,
    var alamat: String? = null,
    var nohp: String? = null,
    var email: String? = null,
    var image_url: String? = null
)

data class KendaraanUserResponse(
    var id: String,
    var merk: String,
    var tipe: String,
    var gambar: String,
    var tanggal_post: String,
    var waktu: String
)
