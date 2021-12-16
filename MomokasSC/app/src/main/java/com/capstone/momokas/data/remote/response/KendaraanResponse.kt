package com.capstone.momokas.data.remote.response

data class Response(
    var listKendaraan: List<KendaraanResponse>? = null
)

data class KendaraanResponse(
    var user_id: String,
    var id: String,
    var merk: String,
    var tipe: String,
    var warna: String,
    var cc: Int,
    var tahun: String,
    var jumlahKm: Int,
    var pajak: String,
    var surat: String,
    var kepemilikan: String,
    var harga: Int,
    var Deskripsi: String,
    var gambar: String,
//    var terjual: Boolean,
    var tanggal_post: String,
    var waktu: String

)