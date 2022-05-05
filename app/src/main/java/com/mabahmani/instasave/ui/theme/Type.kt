package com.mabahmani.instasave.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mabahmani.instasave.R

private val Ubuntu = FontFamily(
    Font(R.font.ubuntu_light, FontWeight.Light),
    Font(R.font.ubuntu_regular, FontWeight.Normal),
    Font(R.font.ubuntu_medium, FontWeight.Medium),
    Font(R.font.ubuntu_bold, FontWeight.Bold)
)

val Cookie = FontFamily(
    Font(R.font.cookie, FontWeight.Normal)
)

val Typography = Typography(

    defaultFontFamily = Ubuntu,

    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
    ),

    h2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),

    h3 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),

    h4 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    button = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
    ),
    overline = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
    )
)