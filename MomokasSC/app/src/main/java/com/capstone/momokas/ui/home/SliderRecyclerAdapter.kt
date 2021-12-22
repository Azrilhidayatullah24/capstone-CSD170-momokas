package com.capstone.momokas.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.momokas.data.remote.response.SliderResponse
import com.capstone.momokas.databinding.ItemRvKendaraanBinding
import com.capstone.momokas.databinding.ItemRvSliderBinding
import com.capstone.momokas.ui.home.SliderRecyclerAdapter.*

class SliderRecyclerAdapter(private val response: List<SliderResponse>) :
    RecyclerView.Adapter<ListViewHolder>() {

    class ListViewHolder(val binding : ItemRvSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRvSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = response[position]
        Glide.with(holder.itemView.context)
            .load(data.url)
            .into(holder.binding.imgSlide)
    }

    override fun getItemCount(): Int = response.size
}