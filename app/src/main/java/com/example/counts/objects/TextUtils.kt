package com.example.counts.objects

open class TextUtils {


    fun isNumeric(input: String): Boolean {
        return input.toFloatOrNull() != null || input.toIntOrNull() != null  // For integers
    }


}