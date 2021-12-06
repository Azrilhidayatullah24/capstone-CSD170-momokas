package com.capstone.momokas.ui.login

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.capstone.momokas.R
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.capstone.momokas.ui.login.RegistModel
import com.google.android.gms.tasks.OnSuccessListener
import android.content.Intent
import com.capstone.momokas.MainActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import com.capstone.momokas.databinding.ActivityForgetpassBinding
import com.capstone.momokas.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityRegisterBinding

    //Deklarasi Variable
    private var myEmail: TextInputEditText? = null
    private var myPassword: TextInputEditText? = null
    private var username: TextInputEditText? = null
    private var nama: TextInputEditText? = null
    private var alamat: TextInputEditText? = null
    private var notelp: TextInputEditText? = null
    private var confirm: TextInputEditText? = null
    private var progressBar: ProgressBar? = null
    private var auth: FirebaseAuth? = null
    private var getEmail: String? = null
    private var getPassword: String? = null
    private val getUsername: String? = null
    private val getnama: String? = null
    private val getalamat: String? = null
    private val getnohp: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        detailBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        detailBinding.toolbar.setNavigationOnClickListener { onBackPressed() }

        //Inisialisasi Widget dan Membuat Objek dari Firebae Authenticaion
        username = findViewById<View>(R.id.inputUsername) as TextInputEditText
        nama = findViewById<View>(R.id.inputName) as TextInputEditText
        alamat = findViewById<View>(R.id.inputAddress) as TextInputEditText
        notelp = findViewById<View>(R.id.inputPhonenumber) as TextInputEditText
        confirm = findViewById<View>(R.id.inputRetype_Password) as TextInputEditText
        myEmail = findViewById<View>(R.id.inputEmail) as TextInputEditText
        myPassword = findViewById<View>(R.id.inputPassword) as TextInputEditText
        val regButtton = findViewById<Button>(R.id.btn_daftar)
        progressBar = findViewById(R.id.progressBar)
        progressBar?.setVisibility(View.GONE)
        auth = FirebaseAuth.getInstance()
        regButtton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                cekDataUser()
            }

            //Method ini digunakan untuk mengecek dan mendapatkan data yang dimasukan user
            private fun cekDataUser() {
                //Mendapatkan dat yang diinputkan User
                getEmail = myEmail!!.text.toString()
                getPassword = myPassword!!.text.toString()
                val getemail = myEmail!!.text.toString()
                val getUsername = username!!.text.toString()
                val getnama = nama!!.text.toString()
                val getalamat = alamat!!.text.toString()
                val getnohp = notelp!!.text.toString()


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
                } else if (isEmpty(getEmail!!) || isEmpty(getPassword!!)) {
                    Toast.makeText(
                        applicationContext,
                        "Email Atau Sandi Tidak Boleh Kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //Mengecek panjang karakter password baru yang akan didaftarkan
                    if (getPassword!!.length < 6) {
                        Toast.makeText(
                            applicationContext,
                            "Sandi Terlalu Pendek, MIN 6 Karakter",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        progressBar?.setVisibility(View.VISIBLE)
                        createUserAccount()
                    }
                }
            }

            //Method ini digunakan untuk membuat akun baru user
            private fun createUserAccount() {
                getEmail = myEmail!!.text.toString()
                getPassword = myPassword!!.text.toString()
                val getUsername = username!!.text.toString()
                val getnama = nama!!.text.toString()
                val getalamat = alamat!!.text.toString()
                val getnohp = notelp!!.text.toString()
                auth!!.createUserWithEmailAndPassword(getEmail!!, getPassword!!)
                    .addOnCompleteListener { task ->
                        //Mendapatkan Instance dari Database
                        val database = FirebaseDatabase.getInstance().reference


                        //Mengecek status keberhasilan saat medaftarkan email dan sandi baru
                        if (task.isSuccessful) {

                            //Mendapatkan UserID dari pengguna yang Terautentikasi
                            val getUserID = auth!!.currentUser!!.uid

                            /*
                    Jika Tidak, maka data dapat diproses dan meyimpannya pada Database
                    Menyimpan data referensi pada Database berdasarkan User ID dari masing-masing Akun
                    */database.child("User").child(getUserID)
                                .setValue(
                                    RegistModel(
                                        getUsername, getnama, getalamat,
                                        getnohp, getEmail
                                    )
                                )
                                .addOnSuccessListener { //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                                    username!!.setText("")
                                    nama!!.setText("")
                                    alamat!!.setText("")
                                    notelp!!.setText("")
                                    myEmail!!.setText("")
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Berhasil",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            Toast.makeText(
                                this@RegisterActivity,
                                "Sign Up Success",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                task.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            progressBar?.setVisibility(View.GONE)
                        }
                    }
            }
        })
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }
}