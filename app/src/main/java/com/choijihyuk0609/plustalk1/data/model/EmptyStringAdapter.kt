package com.choijihyuk0609.plustalk1.data.model

import android.util.Log
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import java.io.IOException

class EmptyStringAdapter : TypeAdapter<Any>() {
    @Throws(IOException::class)
    override fun write(out: com.google.gson.stream.JsonWriter, value: Any?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.toString())
        }
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Any? {
        val value = reader.nextString()
        if (value.isEmpty()){
            Log.d("KKANG", "json값이 비었습니다")
            return null
        } else {
            Log.d("KKANG","${value}")
            return value
        }
    }
}