package com.kitching

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kitching.adapter.TeamAdapter
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.ActivityMainBinding
import com.kitching.view.model.TeamViewModel
import com.kitching.view.model.factory.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var backPressedTime: Long = 0
    private val delayTime = 1500L

    // userId Mock Data
    private val teamId = "3uM01g5GSz8lC49JA6vq"

    private lateinit var teamAdapter: TeamAdapter

    private val viewModel by viewModels<TeamViewModel> {
        ViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val navHost =
            supportFragmentManager.findFragmentById(R.id.navMainFragment) as NavHostFragment?
                ?: return
        navController = navHost.navController

        teamAdapter = TeamAdapter(binding.drawerLayout, this, lifecycleScope, navController)

        lifecycleScope.launch {
            val userId = PreferencesDataSource(this@MainActivity).getUserId()
            if (userId.isNullOrEmpty()) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }

            PreferencesDataSource(this@MainActivity).saveTeamId(teamId)
            repeatOnLifecycle(Lifecycle.State.STARTED) {
              
                viewModel.getTeams(userId.toString())

                viewModel.teams.collectLatest {
                    when (it) {
                        is FirebaseResult.Success -> teamAdapter.submitList(it.data)
                        is FirebaseResult.Loading -> {} // TODO("로딩 처리)
                        is FirebaseResult.Failure -> {} // TODO("예외 처리")
                        is FirebaseResult.DummyConstructor -> {} // TODO("더미 생성")
                    }
                }
            }
        }

        with(binding) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            toolbar.setNavigationOnClickListener {
                // top level destination인지 확인
                if(getRootFragmentsFromBottomNav(bottomNavi).contains(navController.currentDestination?.id)) {
                    drawerLayout.openDrawer(GravityCompat.START)
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }

            bottomNavi.setupWithNavController(navController)

            appBarConfiguration = AppBarConfiguration(getRootFragmentsFromBottomNav(bottomNavi), drawerLayout, ::onSupportNavigateUp)

            setupActionBarWithNavController(navController, appBarConfiguration)

            with(teamListRV) {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = teamAdapter
            }
        }

        addOnBackPressedDispatcher {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawers()
            } else if (navController.currentDestination?.id != R.id.scheduleFragment && navController.previousBackStackEntry != null) {
                navController.popBackStack()
            } else {
                val currentTime = System.currentTimeMillis()
                val intervalTime = currentTime - backPressedTime

                if (intervalTime in 0..delayTime) {
                    finish()
                } else {
                    backPressedTime = currentTime
                    Toast.makeText(
                        applicationContext,
                        "뒤로 버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        } else {
            super.onSupportNavigateUp()
        }
    }

    /** root fragment Set 반환 */
    private fun getRootFragmentsFromBottomNav(bottomNav: BottomNavigationView): Set<Int> {
        val rootFragments = mutableSetOf<Int>()

        for (i in 0 until bottomNav.menu.size()) {
            rootFragments.add(bottomNav.menu.getItem(i).itemId)
        }
        return rootFragments
    }
}

private fun AppCompatActivity.addOnBackPressedDispatcher(backPressed: () -> Unit) {
    onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPressed.invoke()
            }
        }
    )
}