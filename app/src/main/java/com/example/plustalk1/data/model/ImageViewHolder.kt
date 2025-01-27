package com.example.plustalk1.data.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plustalk1.R

class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageView: ImageView = view.findViewById(R.id.imageView)
    val tagTextView: TextView = view.findViewById(R.id.tagTextView)

}