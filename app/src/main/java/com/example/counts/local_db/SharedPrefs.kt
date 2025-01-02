package com.example.counts.local_db

import android.content.Context
import android.util.Log
import com.example.counts.objects.ConstantsUtils

class SharedPrefs {

    private val PREFRENCE_NAME = "EXPENSE"


    fun isKeySaved(context: Context, key: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.contains(key)
    }


    fun saveToSharedPreferences(context: Context, value: Float) {
        val sharedPreferences = context.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putFloat(ConstantsUtils.COUNT_LIMIT, value).apply()
    }


    fun getFromSharedPreferences(context: Context): Float? {
        val sharedPreferences = context.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE)
        return if (sharedPreferences.contains(ConstantsUtils.COUNT_LIMIT)) {
            sharedPreferences.getFloat(ConstantsUtils.COUNT_LIMIT, 0.0f)
        } else {
            Log.e("SharedPreferences", "Key user_key not found")
            0.0f
        }
    }


}