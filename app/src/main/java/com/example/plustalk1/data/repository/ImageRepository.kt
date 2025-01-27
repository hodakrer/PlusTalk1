package com.example.plustalk1.data.repository

import androidx.lifecycle.LiveData
import com.example.plustalk1.data.model.ImageItem
import com.example.plustalk1.data.model.ImageItemDao

class ImageRepository(private val imageItemDao: ImageItemDao) {

    // Retrieve all images
    fun getAllImages(): LiveData<List<ImageItem>> = imageItemDao.getAllImages()

    // Insert an image
    suspend fun insertImage(image: ImageItem) {
        imageItemDao.insertImage(image)
    }

    // Delete an image
    suspend fun deleteImage(image: ImageItem) {
        imageItemDao.deleteImage(image)
    }
}