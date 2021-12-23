package com.capstone.momokas.ui.profile

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.momokas.R
import com.capstone.momokas.databinding.ActivityEditProfileBinding
import com.capstone.momokas.ui.login.RegistModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityEditProfileBinding

    //Deklarasi Variable
    private var myEmail: EditText? = null
    private var username: EditText? = null
    private var nama: EditText? = null
    private var alamat: EditText? = null
    private var notelp: EditText? = null
    private var progressBar: ProgressBar? = null
    private var auth: FirebaseAuth? = null
    private var getEmail: String? = null
    private var getDatabase: FirebaseDatabase? = null
    private var getRefenence: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        detailBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        detailBinding.toolbar.setNavigationOnClickListener { onBackPressed() }

        //Inisialisasi Widget dan Membuat Objek dari Firebae Authenticaion
        username = findViewById(R.id.inputUsername)
        nama = findViewById(R.id.inputName)
        alamat = findViewById(R.id.inputAddress)
        notelp = findViewById(R.id.inputPhonenumber)
        myEmail = findViewById(R.id.inputEmail)
        val regButtton = findViewById<Button>(R.id.btn_edit_profile)
        progressBar = findViewById(R.id.progressBar)
        progressBar?.setVisibility(View.GONE)
        auth = FirebaseAuth.getInstance()
        data

        //Menyembunyikan / Hide Password
        regButtton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                cekDataUser()
            }

            //Method ini digunakan untuk mengecek dan mendapatkan data yang dimasukan user
            private fun cekDataUser() {
                //Mendapatkan dat yang diinputkan User
                getEmail = myEmail?.getText().toString()
                val getemail = myEmail?.getText().toString()
                val getUsername = username?.getText().toString()
                val getnama = nama?.getText().toString()
                val getalamat = alamat?.getText().toString()
                val getnohp = notelp?.getText().toString()

                // Mendapatkan Referensi dari Database
                auth = FirebaseAuth.getInstance()


                //Mengecek apakah email dan sandi kosong atau tidak
                if (isEmpty(getUsername) || isEmpty(getnama) || isEmpty(getalamat)
                    || isEmpty(getnohp) || isEmpty(getemail)
                ) {
                    //Jika Ada, maka akan menampilkan pesan singkan seperti berikut ini.
                    Toast.makeText(
                        applicationContext,
                        "Data tidak boleh ada yang kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    progressBar?.setVisibility(View.VISIBLE)
                    createUserAccount()
                }
            }

            //Method ini digunakan untuk membuat akun baru user
            private fun createUserAccount() {
                getEmail = myEmail?.getText().toString()
                val getUsername = username?.getText().toString()
                val getnama = nama?.getText().toString()
                val getalamat = alamat?.getText().toString()
                val getnohp = notelp?.getText().toString()
                val getUserID = auth!!.currentUser!!.uid

                /*
        Jika Tidak, maka data dapat diproses dan meyimpannya pada Database
        Menyimpan data referensi pada Database berdasarkan User ID dari masing-masing Akun
        */
                val database = FirebaseDatabase.getInstance().reference
                database.child("User").child(getUserID)
                    .setValue(
                        RegistModel(
                            getUsername, getnama, getalamat,
                            getnohp, getEmail
                        )
                    )
                    .addOnSuccessListener { //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                        username?.setText("")
                        nama?.setText("")
                        alamat?.setText("")
                        notelp?.setText("")
                        myEmail?.setText("")
                        Toast.makeText(this@EditProfileActivity, "Berhasil", Toast.LENGTH_SHORT)
                            .show()
                    }
                Toast.makeText(this@EditProfileActivity, "Edit Berhasil", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private val data: Unit
        get() {
            getDatabase = FirebaseDatabase.getInstance()
            getRefenence = getDatabase!!.reference
            val getUserID = auth!!.currentUser!!.uid
            getRefenence!!.child("User").child(getUserID)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        username!!.setText(snapshot.child("username").value.toString())
                        nama!!.setText(snapshot.child("nama").value.toString())
                        alamat!!.setText(snapshot.child("alamat").value.toString())
                        notelp!!.setText(snapshot.child("nohp").value.toString())
                        myEmail!!.setText(snapshot.child("email").value.toString())
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }
}