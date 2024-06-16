package com.example.projetomobile.inflaters

import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.projetomobile.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


@Composable
fun UserProfileXmlLayout() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser


    AndroidView(factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.activity_user_profile, null, false)

        getUserProfileEmail(view, currentUser)

        view
    }, modifier = Modifier.fillMaxSize())


}

// colocar funções de crud no inflater para ele renderiza-las tambem
private fun getUserProfileEmail(view : View, currentUser: FirebaseUser?) {
    currentUser?.let {
        val emailEditText = view.findViewById<EditText>(R.id.textUserEmail)
        emailEditText.setText(currentUser.email)
        emailEditText.isEnabled = false
    }
}

