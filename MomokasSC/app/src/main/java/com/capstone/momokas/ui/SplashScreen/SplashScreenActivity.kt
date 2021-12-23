package com.capstone.momokas.ui.SplashScreen

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.momokas.R
import com.capstone.momokas.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(2000)
                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                } finally {
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
        thread.start()
    }

}