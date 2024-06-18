package com.example.projetomobile.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.projetomobile.screens.Profile
import androidx.navigation.compose.rememberNavController

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Profile(navController = navController)
        }
    }
}
