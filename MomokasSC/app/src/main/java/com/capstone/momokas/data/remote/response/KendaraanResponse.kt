package com.capstone.momokas.data.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class JenisKendaraan(
    val jenisKendaraan: List<ItemKendaraan?>? = null
)

data class ItemKendaraan(
    val itemKendaraan: List<KendaraanResponse?>? = null
)

@Parcelize
data class KendaraanResponse(
    var user_id: String? = null,
    var nama_user: String? = null,
    var jenis: String? = null,
    var lokasi: String? = null,
    var id: String? = null,
    var merk: String? = null,
    var tipe: String? = null,
    var warna: String? = null,
    var cc: Int? = null,
    var tahun: String? = null,
    var jumlahKm: Int? = null,
    var pajak: String? = null,
    var surat: String? = null,
    var kepemilikan: String? = null,
    var harga: Int? = null,
    var Deskripsi: String? = null,
    var gambar: String? = null,
    var transmisi: String? = null,
    var terjual: Boolean? = false,
    var tanggal_post: String? = null,
    var waktu: String? = null

)  : Parcelable