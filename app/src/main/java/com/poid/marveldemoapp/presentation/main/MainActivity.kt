package com.poid.marveldemoapp.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.poid.marveldemoapp.R
import com.poid.marveldemoapp.core.AppApplication
import com.poid.marveldemoapp.databinding.ActivityMainBinding
import com.poid.marveldemoapp.presentation.comics.ComicsListViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var factory: MainViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        setupBottomNavigation()
        observeBadgeCount()
    }

    private fun observeBadgeCount() {
        lifecycleScope.launchWhenStarted {
            viewModel.comicsCounterFlow.collectLatest {
                when(it) {
                    is MainViewModel.Result.Success -> setBadgeNumber(it.count)
                    is MainViewModel.Result.Default -> {/* do nothing */}
                }
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.apply {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            setupWithNavController(navController)
        }
    }

    private fun setBadgeNumber(number: Int) {
        val menuItemId = R.id.item_menu_wishlist

        if (number > 0) {
            binding.bottomNavigation.apply {
                val badge = getOrCreateBadge(menuItemId)
                badge.isVisible = true
                badge.number = number
            }
        } else {
            binding.bottomNavigation.removeBadge(menuItemId)
        }
    }

    private fun initViewModel() {
        val appApplication = application?.applicationContext
        val repositoryComposition = (appApplication as AppApplication).repositoryComposition

        factory = MainViewModel.Factory(repositoryComposition)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }
}