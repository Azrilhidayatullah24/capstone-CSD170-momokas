package com.capstone.momokas.ui.profile

import android.content.Intent
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
import de.hdodenhof.circleimageview.CircleImageView

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

    private val GetUserID: String? = null
    private var profileImage: CircleImageView? = null
    private var foto: FloatingActionButton? = null
    private var progressBar: ProgressBar? = null
    private val mImageUri: Uri? = null
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


}