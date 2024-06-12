package com.example.projetomobile.inflaters

import android.view.LayoutInflater
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.projetomobile.R


@Composable
fun UserProfileXmlLayout() {
    val context = LocalContext.current
    
    AndroidView(factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.activity_user_profile, null, false)
        view
    },
        modifier = Modifier.fillMaxSize())
}