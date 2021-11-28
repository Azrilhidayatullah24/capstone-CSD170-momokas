package com.capstone.momokas.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.capstone.momokas.MainActivity
import com.capstone.momokas.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvMoveForgetpassActivity: TextView = findViewById(R.id.tv_Forgetpass)
        tvMoveForgetpassActivity.setOnClickListener(this)
        val tvMoveRegisterActivity: TextView = findViewById(R.id.tv_register)
        tvMoveRegisterActivity.setOnClickListener(this)
        val tvMoveMainActivity: Button = findViewById(R.id.btn_login)
        tvMoveMainActivity.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_Forgetpass -> {
                val moveIntent = Intent(this@LoginActivity, ForgetpassActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.tv_register -> {
                val moveIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.btn_login -> {
                val moveIntent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }



}