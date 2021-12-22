package com.capstone.momokas.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.databinding.ItemRvKendaraanBinding
import com.capstone.momokas.ui.detail.DetailUserActivity
import java.text.NumberFormat
import java.util.*

class ProfileRecyclerAdapter(private val response: List<KendaraanResponse>) :
    RecyclerView.Adapter<ProfileRecyclerAdapter.ListViewHolder>() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    class ListViewHolder(val binding: ItemRvKendaraanBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRvKendaraanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = response[position]
        with(holder.binding) {
            Glide.with(holder.itemView.context)
                .load(data.gambar)
                .into(imageKendaraan)

            tvJudul?.text = "${data.merk} ${data.tipe}"
            tvTahun.text = data.tahun

            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            tvHarga.text = numberFormat.format(data.harga).toString()

            tvNamaUser.text = data.nama_user
            tvLokasi.text = data.lokasi.toString()
            tvKilometer.text = "${data.jumlahKm.toString()}km"
            tvPajak.text = data.pajak.toString()
            tvTransmisi.text = data.transmisi
        }
        holder.itemView.setOnClickListener {
            val dataKendaraan = KendaraanResponse(
                data.user_id,
                data.nama_user,
                data.jenis,
                data.lokasi,
                data.id,
                data.merk,
                data.tipe,
                data.warna,
                data.cc,
                data.tahun,
                data.jumlahKm,
                data.pajak,
                data.surat,
                data.kepemilikan,
                data.harga,
                data.Deskripsi,
                data.gambar,
                data.transmisi,
                data.terjual,
                data.tanggal_post,
                data.waktu

            )
            val intentDetail = Intent(it.context, DetailUserActivity::class.java)
            intentDetail.putExtra(EXTRA_DATA, dataKendaraan)
            it.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int = response.size

}