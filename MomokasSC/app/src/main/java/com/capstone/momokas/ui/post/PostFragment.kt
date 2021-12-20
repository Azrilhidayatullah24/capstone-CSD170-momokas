package com.capstone.momokas.ui.post

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.momokas.R
import com.capstone.momokas.data.remote.response.KendaraanResponse
import com.capstone.momokas.data.remote.response.KendaraanUserResponse
import com.capstone.momokas.data.remote.response.UserResponse
import com.capstone.momokas.databinding.FragmentPostBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import java.text.SimpleDateFormat
import java.util.*

class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding as FragmentPostBinding

    private val viewModel: PostViewModel by viewModels()

    private val auth = FirebaseAuth.getInstance().currentUser
    private val getRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var photoReference: StorageReference

    //-- variable dropdown --//
    private lateinit var jenisKendaraaan: String
    private lateinit var merk:Array<String>
    private lateinit var tipe:Array<String>
    private lateinit var cc:Array<String>

    private lateinit var merkKendaraan: String
    private lateinit var tipeKendaraan: String
    private lateinit var warnaKendaraan: String
    private lateinit var ccKendaraan: String
    private lateinit var tahunKendaraan: String
    private lateinit var jmlhKmKendaraan: String
    private lateinit var pajakKendaraan: String
    private lateinit var kelengkapanKendaraan: String
    private lateinit var kepemilikanKendaraan: String
    private lateinit var hargaKendaraan: String
    private lateinit var deskripsiKendaraan: String

    private lateinit var mImageUri: Uri
    private lateinit var idImageURL: UUID
    private var PERMISSION_STORAGE = 1
    private var GALLERY_PICK = 2

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
        // DropDown Configuration
        dropDown(jenisKendaraaan)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jenisKendaraaan = arguments?.getString(JENIS_KENDARAAN).toString()
        photoReference = FirebaseStorage.getInstance().getReference("Kendaraan").child(jenisKendaraaan)
        idImageURL = UUID.randomUUID()

        //-- Handle navigation icon press --//
        binding.topAppBar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        // Button Pilih Gambar
        binding.tvPilihGambar.setOnClickListener {
            getPhotoFromStorage(it)
        }

        binding.btnBatalUpload.setOnClickListener {
            cancelUploadImage()
        }

        //-- Button simpan data --//
        binding.btnSimpan.setOnClickListener {
            insertData(jenisKendaraaan)
//                viewModel.getUserData(auth?.uid!!).observe(viewLifecycleOwner, {
//                    Log.e("UserResponse", it.toString())
//                })
        }

//      *NOTE:  UPDATE BTN BATAL UNTUK DELETE GAMBAR YANG TERUPLOAD
//        binding.btnBatal.setOnClickListener {
//            activity?.onBackPressed()
//        }
    }

    //    FUNCTION    //
    @SuppressLint("SimpleDateFormat")
    private fun insertData(jenis: String) {

        with(binding) {
            merkKendaraan = inputMerk.text.toString()
            tipeKendaraan = inputTipe.text.toString()
            warnaKendaraan = inputWarna.text.toString()
            ccKendaraan = inputCC.text.toString()
            tahunKendaraan = inputTahun.text.toString()
            jmlhKmKendaraan = inputKilometer.text.toString()
            pajakKendaraan = inputPajak.text.toString()
            kelengkapanKendaraan = inputKelengkapan.text.toString()
            kepemilikanKendaraan = inputKepemilikan.text.toString()
            hargaKendaraan = inputHarga.text.toString()
            deskripsiKendaraan = inputDeskripsi.text.toString()
        }

        if (merkKendaraan.isNotEmpty() &&
            tipeKendaraan.isNotEmpty() &&
            warnaKendaraan.isNotEmpty() &&
            ccKendaraan.isNotEmpty() &&
            tahunKendaraan.isNotEmpty() &&
            jmlhKmKendaraan.isNotEmpty() &&
            pajakKendaraan.isNotEmpty() &&
            kelengkapanKendaraan.isNotEmpty() &&
            kepemilikanKendaraan.isNotEmpty() &&
            hargaKendaraan.isNotEmpty() &&
            deskripsiKendaraan.isNotEmpty()
        ) {
            binding.textProgressBar.text = getString(R.string.menyimpan_data)
            binding.progressBar.visibility = View.VISIBLE
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val timeFormat = SimpleDateFormat("hh:mm:ss")

            photoReference.child(idImageURL.toString()).downloadUrl
                .addOnSuccessListener {
                    val downloadUrl = it.toString()
                    val dataKendaraan = KendaraanResponse(
                        user_id = auth?.uid!!,
//                        nama_user = dataUser.nama,
//                        lokasi = dataUser.alamat,
                        id = idImageURL.toString(),
                        jenis = jenis,
                        merk = merkKendaraan,
                        tipe = tipeKendaraan,
                        warna = warnaKendaraan,
                        cc = ccKendaraan.toInt(),
                        tahun = tahunKendaraan,
                        jumlahKm = jmlhKmKendaraan.toInt(),
                        pajak = pajakKendaraan,
                        surat = kelengkapanKendaraan,
                        kepemilikan = kepemilikanKendaraan,
                        harga = hargaKendaraan.toInt(),
                        Deskripsi = deskripsiKendaraan,
                        gambar = downloadUrl,
                        tanggal_post = dateFormat.format(Date()),
                        waktu = timeFormat.format(Date())
                    )
                    val kendaraanUser = KendaraanUserResponse(
                        id = idImageURL.toString(),
                        merk = merkKendaraan,
                        tipe = tipeKendaraan,
                        gambar = downloadUrl,
                        tanggal_post = dateFormat.format(Date()),
                        waktu = timeFormat.format(Date())
                    )

                    getRef.child("Kendaraan").child(idImageURL.toString()).setValue(dataKendaraan).addOnSuccessListener {
                        getRef.child("User").child(auth.uid).child("Kendaraan").child(jenis).child(idImageURL.toString()).setValue(kendaraanUser)

                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Data gagal disimpan!", Toast.LENGTH_SHORT).show()
                    }
                }
        } else Toast.makeText(context, "Lengkapi form dulu", Toast.LENGTH_SHORT).show()
    }

    private fun getPhotoFromStorage(view: View?) {
        if (view?.let {
                ContextCompat.checkSelfPermission(
                    it.context, Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (view?.context as Activity), arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSION_STORAGE
            )
        } else {
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(galleryIntent, "SELECT IMAGE"),
                GALLERY_PICK
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_PICK && resultCode == Activity.RESULT_OK) {
            val imageUri = Objects.requireNonNull(data)?.data
            requireContext().let {
                CropImage.activity(imageUri).setAspectRatio(4, 3)
                    .setMinCropWindowSize(300, 170)
                    .start(it, this)
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                mImageUri = Objects.requireNonNull(result).uri
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        binding.textProgressBar.text = getString(R.string.gambar_sedang_diupload)
        binding.progressBar.visibility = View.VISIBLE
        val imagePath = photoReference.child(idImageURL.toString())
        imagePath.putFile(mImageUri).addOnCompleteListener { task: Task<UploadTask.TaskSnapshot?> ->
                if (task.isSuccessful) {
                    //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                    binding.tvPilihGambar.apply {
                        text = idImageURL.toString()
                        isEnabled = false
                    }
                    binding.btnBatalUpload.apply {
                        isEnabled = true
                        visibility = View.VISIBLE
                    }
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Upload berhasil", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context, "Terjadi Kesalahan Silakan Coba Lagi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun cancelUploadImage() {
        binding.textProgressBar.text = getString(R.string.batal_upload)
        binding.progressBar.visibility = View.VISIBLE

        val imagePath = photoReference.child(idImageURL.toString())
        imagePath.delete().addOnSuccessListener {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(context, "Upload berhasil dibatalkan", Toast.LENGTH_SHORT)
                .show()
            binding.tvPilihGambar.apply {
                text = getString(R.string.pilih_gambar)
                isEnabled = true
            }
            binding.btnBatalUpload.apply {
                isEnabled = false
                visibility = View.INVISIBLE
            }
        }.addOnFailureListener {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(
                context, "Maaf, terjadi kesalahan",
                Toast.LENGTH_SHORT
            ).show()
            Log.e("HAPUS_STORAGE", "pesan: ${it.message.toString()}")
        }
    }

    private fun dropDown(jenis: String) {
        when (jenis) {
            "Mobil" -> {
                merk = resources.getStringArray(R.array.merk_mobil)
                cc = resources.getStringArray(R.array.cc_mobil)

                binding.inputMerk.setOnItemClickListener { adapterView, _, i, _ ->
                    merkKendaraan = adapterView.getItemAtPosition(i).toString()
                    dropDownMobil(merkKendaraan)
                }
            }
            "Motor" -> {
                merk = resources.getStringArray(R.array.merk_motor)
                cc = resources.getStringArray(R.array.cc_motor)

                binding.inputMerk.setOnItemClickListener { adapterView, _, i, _ ->
                    merkKendaraan = adapterView.getItemAtPosition(i).toString()
                    dropDownMotor(merkKendaraan)
                }
            }
        }
        val pajak = resources.getStringArray(R.array.pajak)
        val tahun = resources.getStringArray(R.array.tahun_kendaraan)
        val kelengkapan = resources.getStringArray(R.array.kelengkapan_surat)
        val kepemilikan = resources.getStringArray(R.array.kepemilikan)
        val warna = resources.getStringArray(R.array.warna)

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
        //-- dropdown warna --//
        val adapterWarna = ArrayAdapter(requireContext(), R.layout.dropdown_item, warna)
        binding.inputWarna.setAdapter(adapterWarna)
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