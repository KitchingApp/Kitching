package com.kitching

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitching.adapter.TeamAdapter
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.ActivityMainBinding
import com.kitching.view.model.TeamViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var backPressedTime: Long = 0
    private val delayTime = 1500L

    // userId Mock Data
    private val userId = "16jmfxgNDhzDr4SpnFwM"
    private val teamId = "3uM01g5GSz8lC49JA6vq"

    private lateinit var teamAdapter: TeamAdapter

    private val viewModel by viewModels<TeamViewModel> {
        viewModelFactory
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
            PreferencesDataSource(this@MainActivity).saveTeamId(teamId)
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val teams = viewModel.getTeams(userId)
                viewModel.teams.collectLatest {
                    when (it) {
                        is FirebaseResult.Success -> teamAdapter.submitList(it.data)
                        // TODO(mainActivity 코드 분리 방법 모색)
                        is FirebaseResult.Loading -> {} // TODO("로딩 처리)
                        is FirebaseResult.Failure -> {} // TODO("예외 처리")
                        is FirebaseResult.DummyConstructor -> {} // TODO("더미 생성")
                    }
                }
            }
        }

        with(binding) {
            with(bottomNavi) {
                setupWithNavController(navController)
            }

            setSupportActionBar(toolbar)
            val toggle = ActionBarDrawerToggle(
                this@MainActivity, drawerLayout, toolbar,
                R.string.openDrawer,
                R.string.closeDrawer
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

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