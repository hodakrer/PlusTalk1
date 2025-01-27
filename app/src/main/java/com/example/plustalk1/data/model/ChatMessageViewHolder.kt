package com.example.plustalk1.data.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plustalk1.R

class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val messageBubble: TextView = itemView.findViewById(R.id.textViewMessage)
}