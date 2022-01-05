package com.hunseong.chatting

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hunseong.chatting.databinding.ActivitySignupBinding
import com.hunseong.chatting.model.User

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        signupBtn.setOnClickListener {
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            val name = nameEt.text.toString()

            if (email.isBlank() || password.isBlank() || name.isBlank()) {
                Toast.makeText(this@SignupActivity, "모든 값을 채워주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    val user = User(name)
                    val uid = it.result.user?.uid ?: return@addOnCompleteListener
                    Firebase.database.reference.child("Users").child(uid).setValue(user)
                }

        }
    }
}