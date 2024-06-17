package com.example.projetomobile.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetomobile.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class UpdatePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        val currentPasswordEditText = findViewById<EditText>(R.id.editTextCurrentPassword)
        val newPasswordEditText = findViewById<EditText>(R.id.editTextNewPassword)
        val backButton = findViewById<Button>(R.id.buttonBack)
        val confirmButton = findViewById<Button>(R.id.buttonConfirm)

        backButton.setOnClickListener {
            finish() // Voltar à atividade anterior
        }

        confirmButton.setOnClickListener {
            val currentPassword = currentPasswordEditText.text.toString().trim()
            val newPassword = newPasswordEditText.text.toString().trim()

            if (currentPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, insira sua senha atual.", Toast.LENGTH_SHORT)
                    .show()
            } else if (newPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, insira uma nova senha.", Toast.LENGTH_SHORT).show()
            } else if (newPassword.length < 6) {
                Toast.makeText(
                    this,
                    "A nova senha deve ter pelo menos 6 caracteres.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                updatePassword(currentPassword, newPassword)
            }
        }
    }

    private fun updatePassword(currentPassword : String, newPassword : String) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener{ updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(this, "Senha atualizada com sucesso.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val errorMessage = updateTask.exception?.message ?: "Erro desconhecido"
                            Toast.makeText(this, "Falha ao atualizar senha: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val errorMessage = reauthTask.exception?.message ?: "Erro ao reautenticar. Verifique sua senha."
                    Toast.makeText(this, "Falha na reautenticação: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Nenhum usuário autenticado.", Toast.LENGTH_SHORT).show()
        }
    }
}


