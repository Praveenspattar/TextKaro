package com.example.chatapp.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainer
import androidx.transition.Visibility
import com.example.chatapp.ui.fragments.SearchFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.praveen.chatapp.R

class MainActivity : AppCompatActivity() {

    lateinit var addBtn : FloatingActionButton
    lateinit var searchFragment : SearchFragment
    lateinit var fragmentContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fragmentContainer = findViewById(R.id.fragment_container)
        addBtn = findViewById(R.id.addBtn)
        searchFragment = SearchFragment.newInstance()

        addBtn.setOnClickListener {
            it.visibility = View.GONE
            fragmentContainer.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .addToBackStack(null)
                .commit()
        }

    }

    fun showFABtn(){
        addBtn = findViewById(R.id.addBtn)
        addBtn.visibility = View.VISIBLE
        fragmentContainer.visibility = View.GONE
    }

}