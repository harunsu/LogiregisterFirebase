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
import com.example.logiregisterfirebase.ui.MainActivity.Companion.sessionUser
import com.example.logiregisterfirebase.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var imageUri: Uri
    private val REQUEST_IMAGE_CAPTURE = 100

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val logTAG="ProfileLog"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(sessionUser.profileImageUrl).into(ic_profile_img)
        ic_profile_img.isEnabled=false
        name_surname_update_profile.setText(sessionUser.name_surname)
        email_update_editText.setText(sessionUser.email)
        email_update_editText.isEnabled =false
        password_update_editText.setText("********")
        password2_update_editText.visibility=View.GONE
        profile_update_button.visibility=View.GONE
        ic_edit_img.setOnClickListener{

            ic_profile_img.isEnabled=true
            email_update_editText.isEnabled =true
            password_update_editText.isEnabled =true
            password_update_editText.setText("")
            password2_update_editText.visibility=View.VISIBLE
            profile_update_button.visibility=View.VISIBLE


        }


        Log.d(logTAG, "Info "+sessionUser.name_surname+ sessionUser.email)


        ic_profile_img.setOnClickListener {
            Log.d("ProfileFragment", "try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }
}