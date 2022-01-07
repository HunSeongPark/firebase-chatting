package com.hunseong.chatting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hunseong.chatting.R
import com.hunseong.chatting.databinding.ActivityMainBinding
import com.hunseong.chatting.ui.chatlist.ChatFragment
import com.hunseong.chatting.ui.people.PeopleFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNav()
    }

    private fun setBottomNav() = with(binding) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, PeopleFragment()).commit()

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.people_fragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, PeopleFragment()).commit()
                   true
                }
                R.id.chat_fragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, ChatFragment()).commit()
                    true
                }
                else -> {
                    true
                }
            }
        }
    }
}