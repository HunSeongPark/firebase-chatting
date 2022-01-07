package com.hunseong.chatting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hunseong.chatting.databinding.ActivityMessageBinding
import com.hunseong.chatting.fragment.PeopleFragment.Companion.EXTRA_DEST_UID
import com.hunseong.chatting.model.Chat
import com.hunseong.chatting.model.Comment
import com.hunseong.chatting.util.FirebaseKey.CHAT_ROOM_KEY
import com.hunseong.chatting.util.FirebaseKey.COMMENTS_KEY

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding

    private var destUid: String? = null // 자신의 UID
    private var uid: String? = null // 상대방 UID
    private var chatRoomUid: String? = null

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

        uid = auth.currentUser?.uid
        destUid = intent.getStringExtra(EXTRA_DEST_UID)

        if (destUid == null || uid == null) {
            Toast.makeText(this, "사용자를 읽어올 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        checkChatRoom()
        initViews()
    }

    private fun initViews() = with(binding) {
        sendBtn.setOnClickListener {
            val users = mutableMapOf<String, Boolean>()
            users[uid!!] = true
            users[destUid!!] = true

            val chat = Chat(users)
            if (chatRoomUid == null) {
                sendBtn.isEnabled = false
                db.child(CHAT_ROOM_KEY).push().setValue(chat).addOnSuccessListener {
                    checkChatRoom()
                }
            }
            val message = messageEt.text.toString()
            if (message.isBlank()) {
                Toast.makeText(this@MessageActivity, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val comment = Comment(uid!!, message)
            db.child(CHAT_ROOM_KEY).child(chatRoomUid!!).child(COMMENTS_KEY).push()
                .setValue(comment)
        }
    }

    private fun checkChatRoom() {
        db.child(CHAT_ROOM_KEY).orderByChild("users/$uid").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val chat = data.getValue(Chat::class.java) ?: continue
                        if (chat.users.containsKey(destUid)) {
                            chatRoomUid = data.key
                            binding.sendBtn.isEnabled = true
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }
}