package com.example.chatapp.ui.fragments

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
import com.bumptech.glide.Glide
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

        searchImageView.setOnClickListener{
            if (searchEditText.text.isNotEmpty()) {
                searchUserByEmail(searchEditText.text.toString()) {
                    if (it != null) {
                        displayUserData(it, searchProfilePic, searchUserName)
                    } else {
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //Back pressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.navigateToFragment("HomeFragment") // Example
            }
        })

    }

     private fun searchUserByEmail(email: String, onComplete: (User?) -> Unit) {

        firestore.collectionGroup("profile")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onComplete(null)
                } else {
                    //val user = documents.documents[0].toObject(User::class.java)
                    //onComplete(user)
                    for (document in documents) {
                        val user = document.toObject(User::class.java)
                        onComplete(user)
                        break
                    }
                }
            }
            .addOnFailureListener {
                Log.e("mail",it.message.toString())
                onComplete(null)
            }
    }

    private fun displayUserData(user: User, profileImageView: ImageView, usernameTextView: TextView) {
        // Load the profile picture using Glide or any other image loading library
        Glide.with(this)
            .load(user.profilePictureUrl)
            .into(profileImageView)

        // Set the username
        usernameTextView.text = user.username
    }


}