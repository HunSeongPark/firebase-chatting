package com.hunseong.chatting.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hunseong.chatting.MessageActivity
import com.hunseong.chatting.adapter.PeopleAdapter
import com.hunseong.chatting.databinding.FragmentPeopleBinding
import com.hunseong.chatting.model.User

class PeopleFragment : Fragment() {
    private lateinit var binding: FragmentPeopleBinding

    private val peopleAdapter: PeopleAdapter by lazy {
        PeopleAdapter { user ->
            val intent = Intent(requireContext(), MessageActivity::class.java)
            startActivity(intent)
            Log.e("onClick", "$user")
        }
    }

    private val db: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private val users = mutableListOf<User>()

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            users.clear()
            for (data in snapshot.children) {
                val user = data.getValue(User::class.java) ?: continue
                users.add(user)
            }
            peopleAdapter.submitList(users)
        }
        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = peopleAdapter
    }

    override fun onResume() {
        super.onResume()
        db.child("Users").addValueEventListener(listener)
    }

    override fun onStop() {
        super.onStop()
        db.child("Users").removeEventListener(listener)
    }
}