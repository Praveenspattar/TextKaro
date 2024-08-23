package com.example.chatapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.UserRepositoryImpl
import com.example.chatapp.data.viewModel.UserProfileViewModel
import com.example.chatapp.data.viewModel.UserProfileViewModelFactory
import com.example.chatapp.ui.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.praveen.chatapp.R

class SearchFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var searchEditText: EditText
    lateinit var searchImageView: ImageView
    lateinit var searchProfilePic: ImageView
    lateinit var searchUserName: TextView
    lateinit var addUserBtn: Button
    private lateinit var userProfileViewModel: UserProfileViewModel
    private var friendUserId: String = ""

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
        addUserBtn = view.findViewById(R.id.searchAddBtn)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRepository = UserRepositoryImpl(mainActivity)
        val factory = UserProfileViewModelFactory(userRepository)
        userProfileViewModel = ViewModelProvider(this, factory)[UserProfileViewModel::class.java]//UserProfileViewModel(userRepository)
        userProfileViewModel.user.observe(this as LifecycleOwner, Observer { user ->
            if (user != null) {
                friendUserId = user.userId
                displayUserData(user, searchProfilePic, searchUserName)
            } else {
                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
            }
        })

        userProfileViewModel.operationResult.observe(this as LifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(context, "Friend added and chat created successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to add friend", Toast.LENGTH_SHORT).show()
            }
        })

        searchImageView.setOnClickListener{
            if (searchEditText.text.isEmpty() || !isValidEmail(searchEditText.text.toString())) {
                Toast.makeText(context, "Enter Valid Email", Toast.LENGTH_SHORT).show()
            }
            else {
                userProfileViewModel.searchUserByEmail(searchEditText.text.toString())
            }
        }

        addUserBtn.setOnClickListener {
            if (friendUserId == "") {
                Toast.makeText(context,"No User Searched",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            userProfileViewModel.addFriendAndCreateChat(currentUserId,friendUserId)
        }

        //Back pressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.navigateToFragment("HomeFragment")
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

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@gmail.com$".toRegex()
        return email.matches(emailRegex)
    }

}