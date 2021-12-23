package com.capstone.momokas.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.momokas.R
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail_user.*
import java.text.NumberFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

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


    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
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
            btnCheckoutPublic.setText("TERJUAL")
            btnCheckoutPublic.setEnabled(false)
        } else {
            btnCheckoutPublic.setText("CHECKOUT")
            btnCheckoutPublic.setEnabled(true)
        }


        with(binding) {
            Glide.with(this@DetailActivity)
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