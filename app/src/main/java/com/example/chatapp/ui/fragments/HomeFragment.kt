package com.example.chatapp.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.FirebaseUtil
import com.example.chatapp.data.model.UserModel
import com.example.chatapp.data.repository.FriendRepositoryImpl
import com.example.chatapp.data.viewModel.FriendViewModel
import com.example.chatapp.data.viewModel.FriendViewModelFactory
import com.example.chatapp.ui.activities.MainActivity
import com.example.chatapp.ui.adapter.UsersAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.praveen.chatapp.R

class HomeFragment : Fragment() {
    private lateinit var addBtn: FloatingActionButton
    private lateinit var mainActivity: MainActivity
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var allFriends: List<UserModel>
    private lateinit var friendsRv: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var firestore: FirebaseFirestore
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mainActivity = activity as MainActivity
        addBtn = view.findViewById(R.id.addBtn)
        friendsRv = view.findViewById(R.id.friendsRv)

        // Inflate the layout for this fragment
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        allFriends = mutableListOf()
        firestore = FirebaseUtil.getFirestoreInstance()

        val friendRepository = FriendRepositoryImpl(firestore)
        val factory = FriendViewModelFactory(friendRepository)
        friendViewModel = ViewModelProvider(this,factory)[FriendViewModel::class.java]
        friendViewModel.friendsList.observe(mainActivity as LifecycleOwner, Observer {friendList ->
            if (friendList.isNotEmpty()) {
                allFriends = friendList

                usersAdapter = UsersAdapter(mainActivity,allFriends)
                friendsRv.adapter = usersAdapter
                friendsRv.layoutManager = LinearLayoutManager(mainActivity)
            }
        })

        friendViewModel.fetchFriends(currentUserId.toString())

        addBtn.setOnClickListener{
            mainActivity.navigateToFragment("SearchFragment")
        }

    }

}