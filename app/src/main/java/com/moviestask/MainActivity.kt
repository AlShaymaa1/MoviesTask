package com.moviestask

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_main_host_fragment) }
    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.splashFragment,
                R.id.movieDetailsFragment,
                R.id.favoritesFragment
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbarSettings()
    }


    private fun toolbarSettings() {
        setSupportActionBar(toolbar)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(toolbar, NavHostFragment.findNavController(nav_main_host_fragment))
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.listMoviesFragment) {
                toolbar.visibility = View.GONE
            }else {
                toolbar.visibility = View.GONE
                toolbar.menu.clear()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}