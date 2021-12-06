package com.capstone.momokas.ui.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.momokas.R
import com.capstone.momokas.databinding.ActivityMainBinding
import com.capstone.momokas.databinding.FragmentPostBinding
import com.capstone.momokas.ui.home.HomeFragment

class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding as FragmentPostBinding

    private lateinit var postViewModel: PostViewModel

//-- variable dropdown --//
    private lateinit var jenisKendaraaan: String
    private lateinit var merk:Array<String>
    private lateinit var tipe:Array<String>
    private lateinit var cc:Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
//     DropDown Configuration
        dropDown(jenisKendaraaan)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]

        jenisKendaraaan = arguments?.getString(JENIS_KENDARAAN).toString()

        //-- Handle navigation icon press --//
        binding.topAppBar.setNavigationOnClickListener {
            activity?.onBackPressed()
//            parentFragmentManager.beginTransaction().apply {
//                replace(R.id.nav_host_fragment, HomeFragment(), HomeFragment::class.java.simpleName)
//                activity?.finish()
//                commit()
//            }
        }
    }

//    FUNCTION    //

    private fun dropDown(jenis: String) {
        when (jenis) {
            "Mobil" -> {
                merk = resources.getStringArray(R.array.merk_mobil)
                cc = resources.getStringArray(R.array.cc_mobil)

                binding.inputMerk.setOnItemClickListener { adapterView, _, i, _ ->
                    val merkKendaraan = adapterView.getItemAtPosition(i).toString()
                    dropDownMobil(merkKendaraan)
                }
            }
            "Motor" -> {
                merk = resources.getStringArray(R.array.merk_motor)
                cc = resources.getStringArray(R.array.cc_motor)

                binding.inputMerk.setOnItemClickListener { adapterView, _, i, _ ->
                    val merkKendaraan = adapterView.getItemAtPosition(i).toString()
                    dropDownMotor(merkKendaraan)
                }
            }
        }
        val pajak = resources.getStringArray(R.array.pajak)
        val tahun = resources.getStringArray(R.array.tahun_kendaraan)
        val kelengkapan = resources.getStringArray(R.array.kelengkapan_surat)
        val kepemilikan = resources.getStringArray(R.array.kepemilikan)

//-- dropdown merk --//
        val adapterMerk = ArrayAdapter(requireContext(), R.layout.dropdown_item, merk)
        binding.inputMerk.setAdapter(adapterMerk)
//-- dropdown cc --//
        val adapterCC = ArrayAdapter(requireContext(), R.layout.dropdown_item, cc)
        binding.inputCC.setAdapter(adapterCC)
//-- dropdown pajak --//
        val adapterPajak = ArrayAdapter(requireContext(), R.layout.dropdown_item, pajak)
        binding.inputPajak.setAdapter(adapterPajak)
//-- dropdown tahun --//
        val adapterTahun = ArrayAdapter(requireContext(), R.layout.dropdown_item, tahun)
        binding.inputTahun.setAdapter(adapterTahun)
//-- dropdown kelengkapan surat --//
        val adapterKelengkapan = ArrayAdapter(requireContext(), R.layout.dropdown_item, kelengkapan)
        binding.inputKelengkapan.setAdapter(adapterKelengkapan)
//-- dropdown kepemilikan --//
        val adapterKepemilikan = ArrayAdapter(requireContext(), R.layout.dropdown_item, kepemilikan)
        binding.inputKepemilikan.setAdapter(adapterKepemilikan)
    }

    private fun dropDownMobil(merk: String) {
        when (merk) {
            "Mitsubitsi" -> {
                tipe = resources.getStringArray(R.array.tipe_mitsubitsi)
            }
            "Honda" -> {
                tipe = resources.getStringArray(R.array.tipe_hondaMobil)
            }
            "Suzuki" -> {
                tipe = resources.getStringArray(R.array.tipe_suzukiMobil)
            }
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, tipe)
        binding.inputTipe.setAdapter(arrayAdapter)
    }

    private fun dropDownMotor(merk: String) {
        when (merk) {
            "Honda" -> {
                tipe = resources.getStringArray(R.array.tipe_hondaMotor)
            }
            "Yamaha" -> {
                tipe = resources.getStringArray(R.array.tipe_yamaha)
            }
            "Suzuki" -> {
                tipe = resources.getStringArray(R.array.tipe_suzukiMotor)
            }
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, tipe)
        binding.inputTipe.setAdapter(arrayAdapter)
    }

    companion object {
        const val JENIS_KENDARAAN = "jenis_kendaraan"
    }
}