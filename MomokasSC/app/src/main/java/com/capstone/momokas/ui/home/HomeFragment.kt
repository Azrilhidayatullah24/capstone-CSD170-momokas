package com.capstone.momokas.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.SliderResponse
import com.capstone.momokas.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentOrientation = resources.configuration.orientation

        binding.rvKendaraan.apply {
            layoutManager = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 4)
            } else {
                GridLayoutManager(context, 2)
            }
            setHasFixedSize(true)
            this.adapter = adapter
        }

        binding.rvSlider.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            this.adapter = adapter
        }

        viewModel.getListAllKendaraan().observe(viewLifecycleOwner, {
            getKendaraan(it)
        })

        viewModel.getSlider().observe(viewLifecycleOwner, {
            getSlider(it)
        })

        binding.btnSemuaView.setOnClickListener {
            viewModel.getListAllKendaraan().observe(viewLifecycleOwner, {
                getKendaraan(it)
            })
        }

        binding.btnMotorView.setOnClickListener {
            viewModel.getListMotor().observe(viewLifecycleOwner, {
                getKendaraan(it)
            })
        }

        binding.btnMobilView.setOnClickListener {
            viewModel.getListMobil().observe(viewLifecycleOwner, {
                getKendaraan(it)
            })
        }
    }

    private fun getKendaraan(response: List<KendaraanResponse>) {
        val adapter = HomeRecyclerAdapter(response)
        binding.rvKendaraan.adapter = adapter
//        adapter.setOnItemClickCallback(object : TvShowRvAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: TvItem) {
//                val idMovie = data.id
//                showSelectedUser(idMovie)
//            }
//        })
    }

    private fun getSlider(response: List<SliderResponse>) {
        val adapter = SliderRecyclerAdapter(response)
        binding.rvSlider.adapter = adapter
    }
}