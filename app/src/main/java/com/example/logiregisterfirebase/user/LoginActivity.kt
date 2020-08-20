package com.example.logiregisterfirebase.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logiregisterfirebase.MainActivity
import com.example.logiregisterfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {
            performLogin()
        }

        back_to_register_textview.setOnClickListener {
            finish()
        }
    }

    private fun performLogin() {
        val email = email_login_editText.text.toString()
        val password = password_login_editText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
        }


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val uid = it.result!!.user!!.uid
                    Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")

                    val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")


                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)

                            session = SessionManager(applicationContext)
                            session.createLoginSession(user!!)

                            var intent = Intent(applicationContext, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            applicationContext.startActivity(intent)
                            finish()
                            Log.d("Login", "finish")
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })


                } else {
                    Log.d("Login", "else")
                }


            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
