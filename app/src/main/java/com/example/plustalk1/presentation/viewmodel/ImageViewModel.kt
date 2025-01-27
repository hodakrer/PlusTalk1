package com.example.plustalk1.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.plustalk1.data.model.ImageItem
import com.example.plustalk1.data.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewModel(application: Application, private val repository: ImageRepository) : AndroidViewModel(application) {

    // LiveData to hold the list of images
    val images: LiveData<List<ImageItem>> = repository.getAllImages()

    fun insertImage(imageItem: ImageItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertImage(imageItem) // Assuming this method exists in your repository
        }
    }

    fun deleteImage(imageItem: ImageItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteImage(imageItem)
        }
    }

    fun loadImages(){
        viewModelScope.launch {
            repository.getAllImages()
        }
    }

}