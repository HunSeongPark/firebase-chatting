package com.hunseong.chatting.ui.splash

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.hunseong.chatting.ui.login.LoginActivity
import com.hunseong.chatting.R
import com.hunseong.chatting.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_default)

        remoteConfig.fetch()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    remoteConfig.activate()
                }
                displayMessage()
            }
    }

    private fun displayMessage() {
        val splashBackground = remoteConfig.getString("splash_background")
        val caps = remoteConfig.getBoolean("splash_message_caps")
        val message = remoteConfig.getString("splash_message")
        binding.root.setBackgroundColor(Color.parseColor(splashBackground))
        if (caps) {
            AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("확인") { _, _ ->
                    finish()
                }
                .create()
                .show()
        } else {
            Handler().postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }
    }
}