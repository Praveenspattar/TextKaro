package com.example.chatapp.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.FirebaseUtil
import com.example.chatapp.data.model.UserModel
import com.example.chatapp.data.viewModel.UserProfileViewModel
import com.example.chatapp.ui.activities.MainActivity
import com.praveen.chatapp.R

class UsersAdapter(private val mainActivity: MainActivity, private var friendList: List<UserModel>,
    private val userProfileViewModel: UserProfileViewModel
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    val  currentUserId = FirebaseUtil.getFirebaseAuthInstance().currentUser?.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  friendList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val friendUserId = friendList[position].friendUid

        Glide.with(holder.itemView).load(friendList[position].imageUrl).into(holder.itemProfilePic)
        holder.itemUserName.text = friendList[position].name
        holder.itemMessage.text = friendList[position].lastMsg

        holder.itemView.apply {
            setOnClickListener{
                val chatId = if (currentUserId!! < friendUserId) "$currentUserId-$friendUserId" else "$friendUserId-$currentUserId"
                val bundle = Bundle().apply {
                    putString("userName", friendList[position].name)
                    putString("chatId", chatId)
                    putString("imgurl", friendList[position].imageUrl)
                    putString("friendUid",friendUserId)
                }
                mainActivity.navigateToFragment("ChatFragment", bundle)
            }

            setOnLongClickListener{
                if (currentUserId != null) {
                    userProfileViewModel.deleteFriend(currentUserId,friendUserId)
                }
                true
            }

        }

    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemProfilePic : ImageView = itemView.findViewById(R.id.itemProfilePic)
        val itemUserName : TextView = itemView.findViewById(R.id.itemUserName)
        val itemMessage : TextView = itemView.findViewById(R.id.itemMessage)
    }
}