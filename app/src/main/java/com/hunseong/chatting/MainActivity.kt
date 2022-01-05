package com.hunseong.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hunseong.chatting.fragment.PeopleFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, PeopleFragment()).commit()
    }
}