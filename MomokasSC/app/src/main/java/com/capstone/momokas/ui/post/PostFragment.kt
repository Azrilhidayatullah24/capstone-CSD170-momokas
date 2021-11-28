package com.capstone.momokas.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.momokas.R

class PostFragment : Fragment() {

    private lateinit var postViewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postViewModel =
            ViewModelProvider(this)[PostViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_post, container, false)
        val textView: TextView = root.findViewById(R.id.text_post)
        postViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}