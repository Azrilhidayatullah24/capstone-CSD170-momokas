package com.capstone.momokas.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.momokas.R
import com.capstone.momokas.databinding.ActivityForgetpassBinding

class ForgetpassActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityForgetpassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgetpass)

        detailBinding = ActivityForgetpassBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

    }
}