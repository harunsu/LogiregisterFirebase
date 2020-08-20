package com.example.logiregisterfirebase.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.logiregisterfirebase.MainActivity.Companion.sessionUser
import com.example.logiregisterfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    private lateinit var imageUri: Uri
    private val REQUEST_IMAGE_CAPTURE = 100

    private val currentUser = FirebaseAuth.getInstance().currentUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        currentUser?.let { user ->
            Glide.with(this)
                .load(user.photoUrl)
                .into(ic_profile_img)

            Log.d("ProfileFragment", "try to show name: ${user.displayName}")
            Log.d("ProfileFragment", "try to show name: ${sessionUser.name_surname}")
            with(name_surname_update_profile) { name_surname_update_profile.setText(sessionUser.name_surname) }
            email_update_editText.setText(user.email)
        }

        ic_profile_img.setOnClickListener {
            Log.d("ProfileFragment", "try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }
}