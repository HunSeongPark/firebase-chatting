package com.hunseong.chatting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.hunseong.chatting.databinding.ActivitySignupBinding
import com.hunseong.chatting.model.User
import com.hunseong.chatting.util.FirebaseKey.PROFILE_PATH
import com.hunseong.chatting.util.FirebaseKey.USER_KEY

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private var selectedUri: Uri? = null

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    private val db: DatabaseReference by lazy {
        Firebase.database.reference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        signupBtn.setOnClickListener {
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()
            val name = nameEt.text.toString()

            if (email.isBlank() || password.isBlank() || name.isBlank()) {
                Toast.makeText(this@SignupActivity, "모든 값을 채워주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    val uid = it.result.user?.uid ?: return@addOnCompleteListener
                    if (selectedUri != null) {
                        storage.reference.child(PROFILE_PATH).child(uid)
                            .putFile(selectedUri!!)
                            .addOnSuccessListener {
                                storage.reference.child(PROFILE_PATH).child(uid)
                                    .downloadUrl.addOnSuccessListener { uri ->
                                        val url = uri.toString()
                                        val user = User(name, url, uid)
                                        db.child(USER_KEY).child(uid)
                                            .setValue(user)
                                        finish()
                                    }
                            }
                    } else {
                        val user = User(name, "")
                        db.child(USER_KEY).child(uid)
                            .setValue(user)
                        finish()
                    }

                }
        }

        profileIv.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = MediaStore.Images.Media.CONTENT_TYPE
            }
            startActivityForResult(intent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            selectedUri = data?.data
            binding.profileIv.setImageURI(selectedUri)
        }
    }
}