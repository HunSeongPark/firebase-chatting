package com.hunseong.chatting

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hunseong.chatting.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val authStateListener: FirebaseAuth.AuthStateListener by lazy {
        FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth.signOut()
        initViews()
    }

    private fun initViews() = with(binding) {
        signupBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            val email = emailEt.text.toString()
            val password = pwEt.text.toString()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this@LoginActivity, "모든 값을 채워주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful.not()) {
                        Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }
}