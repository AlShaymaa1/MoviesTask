package com.moviestask.features.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.moviestask.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navController =
            Navigation.findNavController(requireActivity(), R.id.nav_main_host_fragment)
        Handler(Looper.getMainLooper()).postDelayed({
            if (navController.currentDestination!!.id == R.id.splashFragment)
                navController.navigate(R.id.action_splashFragment_to_listMoviesFragment)

        }, 1000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


}