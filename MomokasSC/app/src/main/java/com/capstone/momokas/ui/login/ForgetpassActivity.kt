package com.capstone.momokas.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.momokas.R
import com.capstone.momokas.databinding.ActivityForgetpassBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetpassActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityForgetpassBinding

    //Deklarasi Variable
    private var myEmail: EditText? = null
    private var progressBar: ProgressBar? = null
    private var user: FirebaseAuth? = null
    private var getEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgetpass)

        detailBinding = ActivityForgetpassBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        detailBinding.toolbar.setNavigationOnClickListener { onBackPressed() }

        //Inisialisasi Widget dan Membuat Objek dari FirebaeUser
        myEmail = findViewById(R.id.inputResetPassword)
        val reset = findViewById<Button>(R.id.btn_resetpassword)
        progressBar = findViewById(R.id.progressBar)
        progressBar?.setVisibility(View.GONE)
        user = FirebaseAuth.getInstance()
        reset.setOnClickListener {
            progressBar?.setVisibility(View.VISIBLE)
            getEmail = myEmail?.getText().toString().trim { it <= ' ' }

            //Melakukan Proses Reset Password, dengan memasukan alamat email pengguna
            user!!.sendPasswordResetEmail(getEmail!!)
                .addOnCompleteListener { task -> //Mengecek status keberhasilan saat proses reset Password
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgetpassActivity,
                            "Silakan Cek Inbox pada Email Anda",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@ForgetpassActivity,
                            "Terjadi Kesalahan, Silakan Coba Lagi",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar?.setVisibility(View.GONE)
                    }
                }
        }
    }
}
