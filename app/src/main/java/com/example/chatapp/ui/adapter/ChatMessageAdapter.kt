package com.example.chatapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.ChatMessage
import com.praveen.chatapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatMessageAdapter(
    private val currentUserId: String,
    private var chatMessages: List<ChatMessage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].senderId == currentUserId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            SentMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_sent_message, parent, false)
            )
        } else {
            ReceivedMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_received_message, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(chatMessage)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(chatMessage)
        }
    }

    override fun getItemCount(): Int = chatMessages.size

    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.sent_message_text)
        private val timestampTextView: TextView = itemView.findViewById(R.id.sent_message_timestamp)

        fun bind(chatMessage: ChatMessage) {
            messageTextView.text = chatMessage.messageText
            timestampTextView.text = formatTimestamp(chatMessage.timestamp)
        }
    }

    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.received_message_text)
        private val timestampTextView: TextView = itemView.findViewById(R.id.received_message_timestamp)

        fun bind(chatMessage: ChatMessage) {
            messageTextView.text = chatMessage.messageText
            timestampTextView.text = formatTimestamp(chatMessage.timestamp)
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }

    fun updateMessages(newMessages: List<ChatMessage>) {
        chatMessages = newMessages
        notifyDataSetChanged() // Or use more specific methods like notifyItemInserted/Changed
    }
}
