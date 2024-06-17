package com.example.projetomobile.inflaters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.projetomobile.R
import com.example.projetomobile.activities.UpdateEmailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.core.content.ContextCompat.startActivity

@Composable
fun UserProfileXmlLayout() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Estado para armazenar o email do usuário
    val emailState = remember { mutableStateOf(currentUser?.email ?: "") }

    // Lançador para receber o resultado da atividade de atualização de email
    val updateEmailLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedEmail = result.data?.getStringExtra("updated_email")
            if (updatedEmail != null) {
                emailState.value = updatedEmail
            }
        }
    }

    AndroidView(factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.activity_user_profile, null, false)

        // Atualiza o email no layout
        updateEmailInView(view, emailState.value)

        val updateEmailButton = view.findViewById<Button>(R.id.buttonUpdateEmail)
        updateEmailButton.setOnClickListener {
            val intent = Intent(context, UpdateEmailActivity::class.java)
            // Inicia a atividade para atualizar o e-mail
            updateEmailLauncher.launch(intent)
        }

        view
    }, modifier = Modifier.fillMaxSize())
}

// Função auxiliar para atualizar o e-mail na view
private fun updateEmailInView(view: View, email: String) {
    val emailEditText = view.findViewById<EditText>(R.id.textUserEmail)
    emailEditText.setText(email)
    emailEditText.isEnabled = false
}

// Função original para inicializar o email na view
private fun getUserProfileEmail(view: View, currentUser: FirebaseUser?) {
    currentUser?.let {
        updateEmailInView(view, it.email ?: "")
    }
}
