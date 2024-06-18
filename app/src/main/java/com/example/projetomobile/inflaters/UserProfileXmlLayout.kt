package com.example.projetomobile.inflaters

import android.app.Activity
import android.content.Context
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
import androidx.navigation.NavHostController
import com.example.projetomobile.activities.AdvancedSettingsActivity
import com.example.projetomobile.activities.LoginActivity
import com.example.projetomobile.activities.UpdatePasswordActivity

@Composable
fun UserProfileXmlLayout(navController: NavHostController) {
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
            updateEmailLauncher.launch(intent)
        }

        val updatePasswordButton = view.findViewById<Button>(R.id.buttonUpdatePassword)
        updatePasswordButton.setOnClickListener{
            val intent = Intent(context, UpdatePasswordActivity::class.java)
            startActivity(context, intent, null)
        }

        val logoutButton = view.findViewById<Button>(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            handleLogoutClick(context)
        }

        val advancedSettingsButton = view.findViewById<Button>(R.id.buttonAdvancedSettings)
        advancedSettingsButton.setOnClickListener {
            val intent = Intent(context, AdvancedSettingsActivity::class.java)
            startActivity(context, intent, null)
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

private fun handleLogoutClick(context : Context) {
    FirebaseAuth.getInstance().signOut()

    val loginIntent = Intent(context, LoginActivity::class.java)
    loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(loginIntent)
}
