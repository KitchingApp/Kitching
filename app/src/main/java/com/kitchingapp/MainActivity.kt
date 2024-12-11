package com.kitchingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitchingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        with(binding) {
            setSupportActionBar(included.toolbar)
            val toggle = ActionBarDrawerToggle(
                this@MainActivity, drawerLayout, included.toolbar,
                R.string.openDrawer,
                R.string.closeDrawer
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
        }

    }
}