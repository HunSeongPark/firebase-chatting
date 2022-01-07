package com.hunseong.chatting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hunseong.chatting.databinding.ActivityMessageBinding
import com.hunseong.chatting.fragment.PeopleFragment.Companion.EXTRA_DEST_UID
import com.hunseong.chatting.model.Chat
import com.hunseong.chatting.util.FirebaseKey.CHAT_ROOM_KEY

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private var destUid: String? = null

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val db: DatabaseReference by lazy {
        Firebase.database.reference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        destUid = intent.getStringExtra(EXTRA_DEST_UID)
        if (destUid == null) {
            Toast.makeText(this, "사용자를 읽어올 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        initViews()
    }

    private fun initViews() = with(binding) {
        sendBtn.setOnClickListener {
            val uid = auth.currentUser?.uid ?: return@setOnClickListener
            val chat = Chat(uid = uid, destUid = destUid!!)

            db.child(CHAT_ROOM_KEY).push().setValue(chat)
        }
    }
}