package com.example.projetomobile.model

import android.net.Uri

data class Pets(
    var name: String = "",
    var breed: String = "",
    var color: String = "",
    var size: String = "",
    var weight: String = "",
    var imageUri: Uri? = null
)
