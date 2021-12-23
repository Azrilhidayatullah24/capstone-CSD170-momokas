package com.capstone.momokas.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.capstone.momokas.R
import com.capstone.momokas.data.remote.response.UserResponse
import com.capstone.momokas.ui.login.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private var reference: DatabaseReference? = null
    private var database: DatabaseReference? = null
    private var auth: FirebaseUser? = null

    private var getNama: TextView? = null
    private var getUsername: TextView? = null
    private var getAlamat: TextView? = null
    private var getEmail: TextView? = null
    private var getHp: TextView? = null
    private var getDatabase: FirebaseDatabase? = null
    private var getRefenence: DatabaseReference? = null
    private var references: StorageReference? = null

    private var profileImage: CircleImageView? = null
    private var foto: FloatingActionButton? = null
    private var progressBar: ProgressBar? = null
    private var mImageUri: Uri? = null
    var PERMISSION_STORAGE = 1
    var GALLERY_PICK = 2
    private var recyclerView: RecyclerView? = null
    private var btnPost: MaterialButton? = null

    private val photoReference = FirebaseStorage.getInstance().reference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)


        auth = FirebaseAuth.getInstance().currentUser
        profileImage = root.findViewById(R.id.tv_photo_profile)
        getNama = root.findViewById(R.id.tv_nama_user)
        getUsername = root.findViewById(R.id.tv_username)
        getAlamat = root.findViewById(R.id.tv_Address)
        getEmail = root.findViewById(R.id.tv_email)
        getHp = root.findViewById(R.id.tv_Phonenumber)
        progressBar = root.findViewById(R.id.progressBar)
        foto = root.findViewById(R.id.floatingActionButton)
        getDatabase = FirebaseDatabase.getInstance()
        getRefenence = getDatabase?.reference
        recyclerView = root.findViewById(R.id.rvKendaraanUser)
        btnPost = root.findViewById(R.id.btn_post)

        //Mendapatkan Referensi dari Firebase Storage
        references = FirebaseStorage.getInstance().reference
        database = FirebaseDatabase.getInstance().reference
        foto?.setOnClickListener { view: View? ->
            getPhotoFromStorage(view)
        }
        getphoto()

        viewModel.getUserData(auth?.uid!!).observe(viewLifecycleOwner, {
            getData(it)
        })

        recyclerView?.setHasFixedSize(true)

        viewModel.getListKendaraanUser(auth?.uid!!).observe(viewLifecycleOwner, {
            val mAdapter = ProfileRecyclerAdapter(it)
            recyclerView?.adapter = mAdapter
            btnPost?.text = "${mAdapter.itemCount} POST"

        })

        val logout = root?.findViewById<MaterialButton>(R.id.btn_logout)
        logout?.setOnClickListener {
            Toast.makeText(context, "LOGOUT", Toast.LENGTH_SHORT).show()
            context?.let { it1 ->
                AuthUI.getInstance()
                    .signOut(it1)
                    .addOnCompleteListener { task: Task<Void?>? ->
                        // user is now signed out
                        Toast.makeText(context, "LogOut Berhasil", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(context, LoginActivity::class.java))
                        (context as AppCompatActivity).finish()
                    }
            }

        }

        val editProfile = root?.findViewById<MaterialButton>(R.id.btn_edit)
        editProfile?.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }

        return root
    }

    private fun getData(data: UserResponse) {
        getNama?.text = data.nama
        getUsername?.text = data.username
        getAlamat?.text = data.alamat
        getEmail?.text = data.email
        getHp?.text = data.nohp
        progressBar?.visibility = GONE
    }


    //Method Ini Digunakan Untuk Membuka Image dari Galeri atau Kamera
    //Method Ini Digunakan Untuk Menapatkan Hasil pada Activity, dari Proses Yang kita buat sebelumnya
    //Dan Mendapatkan Hasil File Photo dari Galeri atau Kamera
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


    private fun uploadImage() {
        progressBar!!.visibility = View.VISIBLE
        val imageURL = UUID.randomUUID().toString()
        val imagePath = photoReference.child(imageURL)

        imagePath.putFile(mImageUri!!)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    imagePath.downloadUrl
                        .addOnSuccessListener { uri: Uri ->
                            val downloadUrl = uri.toString()
                            database!!.child("PhotoProfil").child(auth!!.uid)
                                .setValue(ImageModel(downloadUrl))
                                .addOnSuccessListener { aVoid: Void? ->
                                    //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                                    Toast.makeText(context, "Upload Berhasil", Toast.LENGTH_SHORT)
                                        .show()
                                    getphoto()
                                }
                        }
                } else {
                    progressBar!!.visibility = GONE
                    Toast.makeText(
                        context, "Terjadi Kesalahan Silakan Coba Lagi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_PICK && resultCode == Activity.RESULT_OK) {
            val imageUri = Objects.requireNonNull(data)?.data
            requireContext().let {
                CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(200, 200)
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

    private fun getphoto() {
        reference = FirebaseDatabase.getInstance().reference
        reference!!.child("PhotoProfil").child(auth!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val dataprofile: ImageModel? = dataSnapshot.getValue(ImageModel::class.java)
                    if ((if (dataprofile != null) dataprofile.image_url else "NULL") != null) {
                        Picasso.get()
                            .load(if (dataprofile != null) dataprofile.image_url else "NULL")
                            .placeholder(R.drawable.ic_baseline_account_circle_200)
                            .into(profileImage)
                        progressBar!!.visibility = GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            )
    }

}