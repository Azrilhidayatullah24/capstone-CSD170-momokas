package com.capstone.momokas.ui.login

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import android.os.Bundle
import com.capstone.momokas.R
import com.google.firebase.auth.FirebaseUser
import android.content.Intent
import com.capstone.momokas.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.capstone.momokas.ui.login.RegisterActivity
import com.capstone.momokas.ui.login.ForgetpassActivity
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.capstone.momokas.ui.detail.DetailActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var myEmail: EditText? = null
    private var myPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var auth: FirebaseAuth? = null
    private var listener: AuthStateListener? = null
    private var getEmail: String? = null
    private var getPassword: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        myEmail = findViewById(R.id.inputEmail)
        myPassword = findViewById(R.id.inputPassword)
        progressBar = findViewById(R.id.progressBar)
        progressBar?.setVisibility(View.GONE)
        val login = findViewById<Button>(R.id.btn_login)
        login.setOnClickListener(this)
        val mendaftar = findViewById<TextView>(R.id.tv_register)
        mendaftar.setOnClickListener(this)
        val lupa = findViewById<TextView>(R.id.tv_Forgetpass)
        lupa.setOnClickListener(this)

        //Instance / Membuat Objek Firebase Authentication
        auth = FirebaseAuth.getInstance()

        //Mengecek Keberadaan User
        listener = AuthStateListener { firebaseAuth ->
            //Mengecek apakah ada user yang sudah login / belum logout
            val user = firebaseAuth.currentUser
            if (user != null) {
                //Jika ada, maka halaman akan langsung berpidah pada MainActivity
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    //Menerapkan Listener
    override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(listener!!)
    }

    //Melepaskan Litener
    override fun onStop() {
        super.onStop()
        if (listener != null) {
            auth!!.removeAuthStateListener(listener!!)
        }
    }

    //Method ini digunakan untuk proses autentikasi user menggunakan email dan kata sandi
    private fun loginUserAccount() {
        auth!!.signInWithEmailAndPassword(getEmail!!, getPassword!!)
            .addOnCompleteListener { task -> //Mengecek status keberhasilan saat login
                if (task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "username atau password salah",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_register -> startActivity(
                Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                )
            )
            R.id.tv_Forgetpass -> startActivity(
                Intent(
                    this@LoginActivity,
                    ForgetpassActivity::class.java
                )
            )
            R.id.btn_login -> {
                //Mendapatkan dat yang diinputkan User
                getEmail = myEmail!!.text.toString()
                getPassword = myPassword!!.text.toString()

                //Mengecek apakah email dan sandi kosong atau tidak
                if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)) {
                    Toast.makeText(this, "Email atau Sandi Tidak Boleh Kosong", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    loginUserAccount()
                    progressBar!!.visibility = View.VISIBLE
                }
            }
        }
    }
}