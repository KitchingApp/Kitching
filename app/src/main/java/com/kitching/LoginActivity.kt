package com.kitching

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitching.databinding.ActivityLoginBinding
import com.kitching.view.fragment.login.LoginMainFragment

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LoginMainFragment())
            .commit()
    }
}