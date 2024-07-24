package com.example.chatapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.chatapp.ui.activities.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.praveen.chatapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var addBtn: FloatingActionButton
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mainActivity = activity as MainActivity

        addBtn = view.findViewById(R.id.addBtn)
        addBtn.setOnClickListener{
            mainActivity.navigateToFragment("SearchFragment")
        }

        // Inflate the layout for this fragment
        return view

    }

}