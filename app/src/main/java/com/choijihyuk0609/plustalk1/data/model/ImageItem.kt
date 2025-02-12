package com.choijihyuk0609.plustalk1.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class ImageItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tag: String,
    val imgUri: Uri // Use Uri for storing image URI
    //val picture: Bitmap //you can erase for original condition
)