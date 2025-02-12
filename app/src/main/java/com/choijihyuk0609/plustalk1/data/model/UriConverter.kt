package com.choijihyuk0609.plustalk1.data.model

import android.net.Uri
import androidx.room.TypeConverter

class UriConverter {

    // Convert Uri to String (to store it in the database)
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    // Convert String back to Uri
    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }
}