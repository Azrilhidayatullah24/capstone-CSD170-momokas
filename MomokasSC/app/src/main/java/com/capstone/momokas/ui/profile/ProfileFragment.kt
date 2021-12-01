package com.capstone.momokas.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.momokas.R
import com.capstone.momokas.ui.login.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val textView: TextView = root.findViewById(R.id.text_profile)
        profileViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
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

        return root
    }



}