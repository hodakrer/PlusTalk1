package com.choijihyuk0609.plustalk1.data.model

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
//For Using ContentProvider, not Room
class ImageItemProvider : ContentProvider( ) {
    override fun delete(uri: Uri,
                        selection: String?,
                        selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate( ): Boolean {
        return false
    }

    override fun query(uri: Uri,
                       projection: Array<String>?,
                       selection: String?,
                       selectionArgs: Array<String>?,
                       sortOrder: String?): Cursor? {
        return null
    }

    override fun update(uri: Uri,
                        values: ContentValues?,
                        selection: String?,
                        selectionArgs: Array<String>?): Int{
        return 0
    }
}