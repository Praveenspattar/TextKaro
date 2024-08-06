package com.example.chatapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.FirebaseUtil
import com.example.chatapp.MyApp
import com.example.chatapp.data.model.ChatMessage
import com.example.chatapp.ui.activities.MainActivity
import com.example.chatapp.ui.adapter.ChatMessageAdapter
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.praveen.chatapp.R
import java.util.UUID

class ChatFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var backImg:ImageView
    lateinit var chatsRv: RecyclerView
    lateinit var chatProfileImg : ImageView
    lateinit var friendNameTv : TextView
    var friendName :String = "userName"
    lateinit var friendUid: String
    lateinit var currentUid: String
    lateinit var imageUrl: String
    lateinit var sendBtn: ImageView
    lateinit var chatId: String
    lateinit var messageReference: CollectionReference
    lateinit var messageEt: EditText
    val messageId = UUID.randomUUID().toString()
    lateinit var chatMessage: ChatMessage
    lateinit var chatMessageAdapter: ChatMessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val args = arguments

        if (args != null) {
            chatId = args.getString("chatId").toString()
        }
        backImg = view.findViewById(R.id.img_back)
        chatsRv = view.findViewById(R.id.chatsRv)
        friendNameTv = view.findViewById(R.id.friendName)
        chatProfileImg = view.findViewById(R.id.chat_profile_img)
        sendBtn = view.findViewById(R.id.sendbtn)
        messageEt = view.findViewById(R.id.msg_edit_text)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageReference = FirebaseUtil.getFirestoreInstance().collection("chats").document(chatId).collection("messages")
        mainActivity = activity as MainActivity
        friendName = arguments?.getString("userName").toString()
        imageUrl = arguments?.getString("imgurl").toString()
        friendUid = arguments?.getString("friendUid").toString()
        currentUid = FirebaseUtil.getFirebaseAuthInstance().currentUser?.uid ?: ""

        Glide.with(mainActivity).load(imageUrl).into(chatProfileImg)
        friendNameTv.text = friendName

        backImg.setOnClickListener{
            mainActivity.navigateToFragment("HomeFragment")
        }

        sendBtn.setOnClickListener {

            chatMessage = ChatMessage(messageId,currentUid,friendUid,
                messageEt.text.toString(),System.currentTimeMillis())

            //val chatId = if (currentUserId < friendUserId) "$currentUserId-$friendUserId" else "$friendUserId-$currentUserId"
            //val messageReference = FirebaseUtil.getFirestoreInstance().collection("chats").document(chatId).collection("messages")

            messageReference.document(messageId)
                .set(chatMessage)
                .addOnSuccessListener {
                    Log.d("Firestore", "Message added successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error adding message", e)
                }

            messageEt.setText("")

        }

        val chatMessages = mutableListOf<ChatMessage>() // Fetch or update this list with your messages

        chatMessageAdapter = ChatMessageAdapter(currentUid,chatMessages)

        fetchMessages()

//        val chatMessageAdapter = FirebaseUtil.getFirebaseAuthInstance().currentUser?.let {
//            ChatMessageAdapter(
//                currentUserId = it.uid/*currentUserId*/,
//                chatMessages = chatMessages
//            )
//        }

        chatsRv.adapter = chatMessageAdapter
        chatsRv.layoutManager = LinearLayoutManager(context) // or context if in a Fragment


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.navigateToFragment("HomeFragment")
            }
        })

    }

    fun fetchMessages() {
        messageReference.orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching messages: ${error.message}")
                    return@addSnapshotListener
                }

                val chatMessages = mutableListOf<ChatMessage>()
                for (document in snapshot!!) {
                    val chatMessage = document.toObject(ChatMessage::class.java)
                    chatMessages.add(chatMessage)
                }
                // Update your RecyclerView adapter with chatMessages
                chatMessageAdapter.updateMessages(chatMessages)
            }
    }



}