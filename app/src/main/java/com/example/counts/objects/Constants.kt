package com.example.counts.objects

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.counts.R

class Constants {


    companion object {
        val getFont = FontFamily(
            Font(R.font.poppins_regular, FontWeight.Light),
            Font(R.font.poppins_medium, FontWeight.Medium),
        )
    }

}