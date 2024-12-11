package com.kitchingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kitchingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var backPressedTime: Long = 0
    private val delayTime = 1500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val navHost = supportFragmentManager.findFragmentById(R.id.navMainFragment) as NavHostFragment? ?: return
        navController = navHost.navController

        with(binding) {
            with(included.bottomNavi) {
                setupWithNavController(navController)
            }

            setSupportActionBar(included.toolbar)
            val toggle = ActionBarDrawerToggle(
                this@MainActivity, drawerLayout, included.toolbar,
                R.string.openDrawer,
                R.string.closeDrawer
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
        }

        addOnBackPressedDispatcher {
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
                binding.drawerLayout.closeDrawers()
            }else if(navController.currentDestination?.id != R.id.scheduleFragment && navController.previousBackStackEntry != null){
                navController.popBackStack()
            }else{
                val currentTime = System.currentTimeMillis()
                val intervalTime = currentTime - backPressedTime

                if (intervalTime in 0..delayTime) {
                    finish()
                } else {
                    backPressedTime = currentTime
                    Toast.makeText(applicationContext,
                        "뒤로 버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

private fun AppCompatActivity.addOnBackPressedDispatcher(backPressed: () -> Unit ){
    onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPressed.invoke()
            }
        }
    )
}