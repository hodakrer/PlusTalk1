package com.choijihyuk0609.plustalk1.data.model


import androidx.recyclerview.widget.DiffUtil

class FriendDiffCallback : DiffUtil.ItemCallback<Friend>() {

    // 항목이 동일한지 비교하는 메서드
    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        // 두 Friend 객체가 동일한지 비교 (예: 이메일을 기준으로)
        return oldItem.email == newItem.email
    }

    // 항목의 내용이 동일한지 비교하는 메서드
    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        // Friend 객체의 모든 내용이 동일한지 확인
        return oldItem == newItem
    }
}