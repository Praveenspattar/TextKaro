package com.example.chatapp.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.FirebaseUtil
import com.example.chatapp.data.model.UserModel
import com.example.chatapp.data.repository.FriendRepositoryImpl
import com.example.chatapp.data.repository.UserRepositoryImpl
import com.example.chatapp.data.viewModel.FriendViewModel
import com.example.chatapp.data.viewModel.FriendViewModelFactory
import com.example.chatapp.data.viewModel.UserProfileViewModel
import com.example.chatapp.data.viewModel.UserProfileViewModelFactory
import com.example.chatapp.ui.activities.CreateAccount
import com.example.chatapp.ui.activities.MainActivity
import com.example.chatapp.ui.activities.ProfileActivity
import com.example.chatapp.ui.adapter.UsersAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.praveen.chatapp.R

class HomeFragment : Fragment() {
    private lateinit var addBtn: FloatingActionButton
    private lateinit var menuBtn: ImageView
    private lateinit var mainActivity: MainActivity
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var userProfileViewModel: UserProfileViewModel
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
        menuBtn = view.findViewById(R.id.menuBtn)

        // Inflate the layout for this fragment
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allFriends = mutableListOf()
        firestore = FirebaseUtil.getFirestoreInstance()

        menuBtn.setOnClickListener {
            val popupMenu = PopupMenu(mainActivity, menuBtn)
            popupMenu.menuInflater.inflate(R.menu.homemenu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit_profile -> {
                        val intent = Intent(mainActivity,ProfileActivity::class.java)
                        mainActivity.startActivity(intent)
                        true
                    }
                    R.id.logout -> {
                        FirebaseUtil.getFirebaseAuthInstance().signOut()
                        val intent = Intent(mainActivity,CreateAccount::class.java)
                        mainActivity.startActivity(intent)
                        mainActivity.finish()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }


        val userRepository = UserRepositoryImpl(mainActivity)
        val factory1 = UserProfileViewModelFactory(userRepository)
        userProfileViewModel = ViewModelProvider(this, factory1)[UserProfileViewModel::class.java]
        userProfileViewModel.deletionResult.observe(viewLifecycleOwner, Observer { sucess->
            if (!sucess) {
                Toast.makeText(context, "Deleting friend failed ", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Deleted friend Successfully ", Toast.LENGTH_SHORT).show()
                friendViewModel.fetchFriends(currentUserId.toString())
            }
        })

        val friendRepository = FriendRepositoryImpl(firestore)
        val factory = FriendViewModelFactory(friendRepository)
        friendViewModel = ViewModelProvider(this,factory)[FriendViewModel::class.java]
        friendViewModel.friendsList.observe(mainActivity as LifecycleOwner, Observer {friendList ->
            if (friendList.isNotEmpty()) {
                allFriends = friendList

                usersAdapter = UsersAdapter(mainActivity,allFriends,userProfileViewModel)
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