package com.capstone.momokas.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.Response
import com.capstone.momokas.databinding.ItemRvKendaraanBinding
import com.capstone.momokas.ui.home.HomeRecyclerAdapter.*

class HomeRecyclerAdapter(private val  response: List<KendaraanResponse>): RecyclerView.Adapter<ListViewHolder>() {

    class ListViewHolder (val binding: ItemRvKendaraanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding= ItemRvKendaraanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = response[position]
        with(holder.binding) {
            Glide.with(holder.itemView.context)
                .load(data.gambar)
                .into(imageKendaraan)
            tvJudul.text = "${data.merk} ${data.tipe} ${data.tahun}"
        }
    }

    override fun getItemCount(): Int = response.size
}