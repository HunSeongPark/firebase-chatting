package com.hunseong.chatting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hunseong.chatting.R
import com.hunseong.chatting.ui.people.PeopleFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, PeopleFragment()).commit()
    }
}