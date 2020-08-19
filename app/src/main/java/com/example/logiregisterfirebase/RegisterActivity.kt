package com.example.logiregisterfirebase

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        val TAG = "RegisterActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        profile_update_button.setOnClickListener{
            performRegister()
        }


        already_have_account_textView.setOnClickListener{
            Log.d("RegisterActivity", "Try to show login activity :" )

            // launch login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        select_photo_button_register.setOnClickListener {
            Log.d("RegisterActivity", "try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceed qnd check what the selected image was
            Log.d("RegisterActivity", "Photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap)

            select_photo_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }


    private fun performRegister(){

        val name = name_register_editText.text.toString()
        val email = email_register_editText.text.toString()
        val password = password_register_editText.text.toString()
        val password2 = password2_register_editText.text.toString()

        if (name.isEmpty() || name.length < 3) {
            Toast.makeText(this, "Please enter the name at least 3 characters", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if(password.isEmpty() || password.length < 8 || password.length > 16){
            Toast.makeText(this, "Please enter password between 8 and 16 characters", Toast.LENGTH_SHORT).show()
            return
        }else if(password2.isEmpty()){
            Toast.makeText(this, "Please confirm your password ", Toast.LENGTH_SHORT).show()
            return
        }else if (password != password2){
            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is :" + email)
        Log.d("RegisterActivity", "Password is : $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(!it.isSuccessful) return@addOnCompleteListener

            //else if succesfull
            Log.d("Main", "Successfully created user uid : ${it.result?.user?.uid}")
            uploadImageToFirebaseStorage()

        }
            .addOnFailureListener{
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user : ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File Location: ${it}")
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl:String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, name_register_editText.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }

    override fun onBackPressed() {

    }



}

class User(val uid: String, val username: String, val profileImageUrl: String)