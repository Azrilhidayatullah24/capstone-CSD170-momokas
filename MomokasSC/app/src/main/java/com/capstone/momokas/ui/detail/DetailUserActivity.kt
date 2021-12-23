package com.capstone.momokas.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.databinding.ActivityDetailUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail_user.*
import java.text.NumberFormat
import java.util.*


class DetailUserActivity : AppCompatActivity() {
    private var tvUser_id: String? = null
    private var tvNama_user: String? = null
    private var tvJenis: String? = null
    private var tvLokasi: String? = null
    private var tvId: String? = null
    private var tvMerk: String? = null
    private var tvTipe: String? = null
    private var tvWarna: String? = null
    private var tvCc: String? = null
    private var tvTahun: String? = null
    private var tvJumlahKm: String? = null
    private var tvPajak: String? = null
    private var tvSurat: String? = null
    private var tvKepemilikan: String? = null
    private var tvHarga: String? = null
    private var tvDeskripsi: String? = null
    private var tvGambar: String? = null
    private var tvTransmisi: String? = null
    private var tvTerjual: String? = null
    private var tvTanggal_post: String? = null
    private var tvWaktu: String? = null
    private var isSold: Boolean? = false
    private var btnTerjual: Button? = null
    private val getRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var idImageURL: UUID


    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var binding: ActivityDetailUserBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        setContentView(binding.root)
        setData()
    }

    @SuppressLint("SetTextI18n", "StringFormatInvalid")
    private fun setData() {
        val listKendaraan = intent.getParcelableExtra(EXTRA_DATA) as KendaraanResponse?
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)

        tvUser_id = listKendaraan?.user_id
        tvNama_user = listKendaraan?.nama_user
        tvJenis = listKendaraan?.jenis
        tvLokasi = listKendaraan?.lokasi
        tvId = listKendaraan?.id
        tvMerk = listKendaraan?.merk
        tvTipe = listKendaraan?.tipe
        tvWarna = listKendaraan?.warna
        tvCc = listKendaraan?.cc.toString()
        tvTahun = listKendaraan?.tahun
        tvJumlahKm = listKendaraan?.jumlahKm.toString()
        tvPajak = listKendaraan?.pajak
        tvSurat = listKendaraan?.surat
        tvKepemilikan = listKendaraan?.kepemilikan
        tvHarga = listKendaraan?.harga.toString()
        tvDeskripsi = listKendaraan?.Deskripsi
        tvGambar = listKendaraan?.gambar
        tvTransmisi = listKendaraan?.transmisi
        tvTerjual = listKendaraan?.terjual.toString()
        tvTanggal_post = listKendaraan?.tanggal_post
        tvWaktu = listKendaraan?.waktu
        isSold = listKendaraan?.terjual

        if (isSold == true) {
            btnCheckout.setText("TERJUAL")
            btnCheckout.setEnabled(false)
        } else {
            btnCheckout.setText("BELUM TERJUAL")
            btnCheckout.setEnabled(true)
        }

        btnCheckout?.setOnClickListener {
            AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Apakah Kendaraan ini sudah laku Terjual?")
                .setPositiveButton("Ya") { dialog, which ->


                    getRef.child("Kendaraan")
                        .child(tvId.toString())
                        .child("terjual")
                        .setValue(true)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Barang Sudah Terjual!", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Data gagal disimpan!", Toast.LENGTH_SHORT).show()
                        }

                }
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }


        with(binding) {
            Glide.with(this@DetailUserActivity)
                .load(listKendaraan?.gambar)
                .into(imgKendaraan)

            inputTipe.text = listKendaraan?.tipe
            inputMerk.text = listKendaraan?.merk
            inputWarna.text = ": ${listKendaraan?.warna}"
            inputCC.text = ": ${listKendaraan?.cc.toString()}"
            inputTahun.text = ": ${listKendaraan?.tahun}"
            inputKilometer.text = ": ${listKendaraan?.jumlahKm.toString()} km"
            inputPajak.text = ": ${listKendaraan?.pajak}"
            inputKelengkapan.text = ": ${listKendaraan?.surat}"
            inputKepemilikan.text = ": ${listKendaraan?.kepemilikan}"
            inputDeskripsi.text = listKendaraan?.Deskripsi
            inputHarga.text = numberFormat.format(listKendaraan?.harga).toString()
        }

    }
}