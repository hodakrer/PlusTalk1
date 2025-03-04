package com.choijihyuk0609.plustalk1.data.model
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.R


class ChatMessageAdapter(private val messageList: List<ChatMessage>, private val memberEmail: String) :
    RecyclerView.Adapter<ChatMessageAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewMessage: TextView = view.findViewById(R.id.textViewMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val messages = messageList[position]

        // 유저의 메시지 여부 확인
        var isUserMessage = false
        if (memberEmail == messages.senderEmail) {
            isUserMessage = true
        }

        // 텍스트 설정
        holder.textViewMessage.text = messages.messageText

        // 레이아웃 파라미터 설정
        val params = holder.textViewMessage.layoutParams as ConstraintLayout.LayoutParams

        if (isUserMessage) {
            // 유저의 메시지: 오른쪽 정렬
            params.startToStart = ConstraintLayout.LayoutParams.UNSET
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            params.marginStart = 0
            params.marginEnd = 16.dpToPx() // 오른쪽 여백
        } else {
            // 상대방의 메시지: 왼쪽 정렬
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.endToEnd = ConstraintLayout.LayoutParams.UNSET
            params.marginStart = 16.dpToPx() // 왼쪽 여백
            params.marginEnd = 0
        }

        holder.textViewMessage.layoutParams = params
    }

    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun getItemCount(): Int = messageList.size
}