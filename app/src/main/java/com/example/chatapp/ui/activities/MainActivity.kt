package com.example.chatapp.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.Observer
import androidx.transition.Visibility
import com.example.chatapp.data.viewModel.FragmentViewModel
import com.example.chatapp.ui.fragments.ChatFragment
import com.example.chatapp.ui.fragments.HomeFragment
import com.example.chatapp.ui.fragments.SearchFragment
import com.example.chatapp.utils.replaceFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.praveen.chatapp.R

class MainActivity : AppCompatActivity() {

    private val fragmentViewModel: FragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fragmentViewModel.currentFragment.observe(this, Observer { fragmentData ->
            val fragmentName = fragmentData.first
            val args = fragmentData.second
            val fragment = when (fragmentName) {
                "SearchFragment" -> SearchFragment().apply { arguments = args }
                "HomeFragment" -> HomeFragment().apply { arguments = args }
                "ChatFragment" -> ChatFragment().apply { arguments = args }
                else -> SearchFragment().apply { arguments = args }
            }
            replaceFragment(fragment, R.id.fragment_container)
        })

        navigateToFragment("HomeFragment")

    }

    fun navigateToFragment(fragmentName: String, args: Bundle? = null) {
        fragmentViewModel.setCurrentFragment(fragmentName, args)
    }

}