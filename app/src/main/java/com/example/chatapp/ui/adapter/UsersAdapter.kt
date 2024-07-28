package com.example.chatapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.data.model.UserModel
import com.praveen.chatapp.R

class UsersAdapter(private var list: List<UserModel>) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        Glide.with(holder.itemView).load(list[position].imageUrl).into(holder.itemProfilePic)
        holder.itemUserName.text = list[position].name
        holder.itemMessage.text = list[position].lastMsg
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemProfilePic : ImageView = itemView.findViewById(R.id.itemProfilePic)
        val itemUserName : TextView = itemView.findViewById(R.id.itemUserName)
        val itemMessage : TextView = itemView.findViewById(R.id.itemMessage)
    }
}