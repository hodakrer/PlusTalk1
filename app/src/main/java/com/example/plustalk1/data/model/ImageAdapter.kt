package com.example.plustalk1.data.model

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.plustalk1.R
import coil.load
import com.example.plustalk1.presentation.viewmodel.ImageViewModel
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.Glide


class ImageAdapter(
    private var images: List<ImageItem>,
    private val onItemClick: (ImageItem) -> Unit,
    private val imageViewModel: ImageViewModel, // Pass the ViewModel
    private val context: Context
) : RecyclerView.Adapter<ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = images[position]
        holder.tagTextView.text = item.tag
        // Use Glide to load the image and handle placeholder and error images

        Glide.with(context)
            .load(item.imgUri)
            .placeholder(R.drawable.placeholder)  // Replace with a drawable resource for placeholder
            .error(R.drawable.placeholder)  // Replace with a drawable resource for error image
            .into(holder.imageView)
            .clearOnDetach()

        Log.d("ImageAdapter", "Finished loading image for URI: ${item.imgUri}")


        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

        holder.itemView.setOnLongClickListener { view ->
            showPopupMenu(view, item)
            true
        }
    }

    private fun showPopupMenu(view: View, item: ImageItem) {
        val popupMenu = PopupMenu(view.context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_image_options, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_erase -> {
                    // Call deleteImage from the ViewModel directly in the Fragment
                    imageViewModel.deleteImage(item) // Delete the image through ViewModel
                    true
                }
                R.id.action_cancel -> {
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
    override fun getItemCount(): Int = images.size
    // Add a function to update the list when data changes
    fun updateImages(newImages: List<ImageItem>) {
        images = newImages
        notifyDataSetChanged()  // Notify the adapter that the list has changed
    }
}