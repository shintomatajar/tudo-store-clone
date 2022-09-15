package com

import com.tudomart.store.ui.activities.auth.MainActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.tudomart.store.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time. com.tudomart.store.ui.activities.auth.
        Handler().postDelayed({
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}