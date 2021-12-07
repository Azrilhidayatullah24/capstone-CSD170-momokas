package com.capstone.momokas.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.ViewModelProvider
import com.capstone.momokas.R
import com.capstone.momokas.ui.login.LoginActivity
import com.capstone.momokas.ui.login.RegistModel
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private var reference: DatabaseReference? = null
    private var database: DatabaseReference? = null
    private var auth: FirebaseUser? = null

    private var getNama: TextView? = null
    private  var getUsername:TextView? = null
    private  var getAlamat:TextView? = null
    private  var getEmail:TextView? = null
    private  var getHp:TextView? = null
    private var getDatabase: FirebaseDatabase? = null
    private var getRefenence: DatabaseReference? = null
    private var references: StorageReference? = null

    private var profileImage: CircleImageView? = null
    private var foto: FloatingActionButton? = null
    private var progressBar: ProgressBar? = null
    private var mImageUri: Uri? = null
    var PERMISSION_STORAGE = 1
    var GALLERY_PICK = 2

    private val photoReference = FirebaseStorage.getInstance().reference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        profileViewModel.text.observe(viewLifecycleOwner, {
        })

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
        getRefenence = getDatabase?.getReference()

        //Mendapatkan Referensi dari Firebase Storage
        references = FirebaseStorage.getInstance().reference
        database = FirebaseDatabase.getInstance().reference
        foto?.setOnClickListener { view: View? ->
            getPhotoFromStorage(view)
        }
        getphoto()
        GetData()

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


    private fun GetData() {
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().reference
        reference?.child("User")?.child(auth!!.uid)
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val dataprofile: RegistModel? = dataSnapshot.getValue(RegistModel::class.java)
                    getNama?.text = dataprofile?.nama
                    getUsername?.text = dataprofile?.username
                    getAlamat?.text = dataprofile?.alamat
                    getEmail?.text = dataprofile?.email
                    getHp?.text = dataprofile?.nohp
                    progressBar?.visibility = GONE
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    /*
                Kode ini akan dijalankan ketika ada error dan
                pengambilan data error tersebut lalu memprint error nya
                ke LogCat
               */
                    Log.e("fragment_profile", databaseError.details + " " + databaseError.message)
                }
            })
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
            .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot?> ->
                if (task.isSuccessful) {
                    photoReference.child(imageURL).downloadUrl
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