package com.capstone.momokas.ui.BottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.momokas.R
import com.capstone.momokas.databinding.FragmentBottomSheetBinding
import com.capstone.momokas.ui.post.PostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding as FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPilihMobil.setOnClickListener(this)
        binding.btnPilihMotor.setOnClickListener(this)
    }

    override fun onClick(btn: View?) {
        when (btn) {
            binding.btnPilihMobil -> {
                val postFragment = PostFragment()

                val bundle = Bundle()
                bundle.putString(PostFragment.JENIS_KENDARAAN, "Mobil")

                postFragment.arguments = bundle

                parentFragmentManager.beginTransaction()?.apply {
                    replace(
                        R.id.nav_host_fragment,
                        postFragment,
                        PostFragment::class.java.simpleName
                    )
                    addToBackStack(null)
                    commit()
                }
                this.dismiss()
            }
            binding.btnPilihMotor -> {
                val postFragment = PostFragment()

                val bundle = Bundle()
                bundle.putString(PostFragment.JENIS_KENDARAAN, "Motor")

                postFragment.arguments = bundle

                parentFragmentManager?.beginTransaction()?.apply {
                    replace(
                        R.id.nav_host_fragment,
                        postFragment,
                        PostFragment::class.java.simpleName
                    )
                    addToBackStack(null)
                    commit()
                }
                this.dismiss()
            }
        }
    }
}