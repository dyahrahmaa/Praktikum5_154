package com.dyah.praktikum5_154

import android.net.Uri

data class Post(
    var username: String,
    var caption: String,
    var imageRes: Int = 0,
    var imageUri: Uri? = null
)
