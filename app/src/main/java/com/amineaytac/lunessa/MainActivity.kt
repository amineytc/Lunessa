package com.amineaytac.lunessa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.amineaytac.lunessa.databinding.ActivityMainBinding
import com.amineaytac.lunessa.util.gone
import com.amineaytac.lunessa.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavView.itemActiveIndicatorColor = null

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment,
                R.id.loginFragment2,
                R.id.signupFragment,
                R.id.basketFragment2,
                R.id.filterItemFragment,
                R.id.filterFragment,
                R.id.onBoardingFragment -> {
                    binding.bottomNavView.gone()
                }

                else -> {
                    binding.bottomNavView.visible()
                }
            }
        }
    }
}