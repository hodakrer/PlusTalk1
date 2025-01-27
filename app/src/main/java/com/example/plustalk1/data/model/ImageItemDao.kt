package com.example.plustalk1.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageItemDao {
    @Insert
    suspend fun insertImage(imageItem: ImageItem)

    @Delete
    suspend fun deleteImage(imageItem: ImageItem)

    @Query("SELECT * FROM image_table")
    fun getAllImages(): LiveData<List<ImageItem>>
}