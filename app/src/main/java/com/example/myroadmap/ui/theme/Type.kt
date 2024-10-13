package com.example.myroadmap.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myroadmap.R

val font = FontFamily(Font(R.font.noto_sans_regular))

val MyTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall=TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp,
        color = Color.Black
    ),
    titleSmall = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,

    ),
)
