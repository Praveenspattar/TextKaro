package com.example.chatapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chatapp.data.repository.UserRepositoryImpl
import com.example.chatapp.data.viewModel.UserProfileViewModel
import com.example.chatapp.data.viewModel.UserProfileViewModelFactory
import com.example.chatapp.ui.activities.MainActivity
import com.example.chatapp.utils.User
import com.google.firebase.firestore.FirebaseFirestore
import com.praveen.chatapp.R

class SearchFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var searchEditText: EditText
    lateinit var searchImageView: ImageView
    lateinit var searchProfilePic: ImageView
    lateinit var searchUserName: TextView
    lateinit var searchAddBtn: Button
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var userProfileViewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        mainActivity = activity as MainActivity
        searchEditText = view.findViewById(R.id.searchEditText)
        searchImageView = view.findViewById(R.id.searchImageView)
        searchProfilePic = view.findViewById(R.id.searchProfilePic)
        searchUserName = view.findViewById(R.id.searchUserName)
        searchAddBtn = view.findViewById(R.id.searchAddBtn)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userRepository = UserRepositoryImpl(mainActivity)
        val factory = UserProfileViewModelFactory(userRepository)
        userProfileViewModel = ViewModelProvider(this, factory)[UserProfileViewModel::class.java]//UserProfileViewModel(userRepository)
        userProfileViewModel.user.observe(this as LifecycleOwner, Observer { user ->
            if (user != null) {
                displayUserData(user, searchProfilePic, searchUserName)
            } else {
                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
            }
        })

        searchImageView.setOnClickListener{
            if (searchEditText.text.isNotEmpty()) {
                userProfileViewModel.searchUserByEmail(searchEditText.text.toString())
            }
        }

        //Back pressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.navigateToFragment("HomeFragment") // Example
            }
        })

    }

    private fun displayUserData(user: User, profileImageView: ImageView, usernameTextView: TextView) {
        Glide.with(this)
            .load(user.profilePictureUrl)
            .into(profileImageView)

        // Set the username
        usernameTextView.text = user.username
    }


}