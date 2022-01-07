package com.hunseong.chatting.ui.chatlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hunseong.chatting.databinding.FragmentChatBinding
import com.hunseong.chatting.model.Chat
import com.hunseong.chatting.model.ChatRoom
import com.hunseong.chatting.model.User
import com.hunseong.chatting.util.FirebaseKey.CHAT_ROOM_KEY
import com.hunseong.chatting.util.FirebaseKey.USER_KEY

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var uid: String
    private val chatListAdapter: ChatListAdapter by lazy {
        ChatListAdapter()
    }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val db: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private val chatList = mutableListOf<ChatRoom>()
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            chatList.clear()
            for (data in snapshot.children) {
                val chat = data.getValue(Chat::class.java) ?: continue
                for (userUid in chat.users.keys) {
                    if (userUid != uid) {
                        db.child(USER_KEY).child(userUid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val user = snapshot.getValue(User::class.java) ?: return
                                    Log.e("ChangeL", "$user", )

                                    val chatRoom = ChatRoom(user, chat.comments)
                                    Log.e("ChangeL", "$chatRoom", )
                                    chatList.add(chatRoom)
                                    chatListAdapter.submitList(chatList)
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                }
            }

            Log.e("ChangeL", "$chatList", )
            chatListAdapter.notifyItemChanged(chatList.lastIndex)
        }

        override fun onCancelled(error: DatabaseError) {}

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uid = auth.currentUser?.uid!!
        initViews()
    }

    private fun initViews() = with(binding) {
        db.child(CHAT_ROOM_KEY).orderByChild("users/$uid").equalTo(true).addListenerForSingleValueEvent(listener)
        recyclerView.adapter = chatListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        db.child(CHAT_ROOM_KEY).orderByChild("users/$uid").equalTo(true).removeEventListener(listener)
    }
}