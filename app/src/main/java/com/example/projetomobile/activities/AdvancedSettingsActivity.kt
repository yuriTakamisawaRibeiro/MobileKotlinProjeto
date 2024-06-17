package com.example.projetomobile.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projetomobile.R
import com.google.firebase.auth.FirebaseAuth

class AdvancedSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_advanced_settings)

        val backButton = findViewById<Button>(R.id.buttonBack)
        val deleteAccountButton = findViewById<Button>(R.id.buttonDeleteAccount)

        backButton.setOnClickListener {
            finish()
        }

        deleteAccountButton.setOnClickListener {
            showDeleteAccountDialog()
        }
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Excluir conta")
            setMessage("Tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.")

            setPositiveButton("Sim") { dialog, which ->
                deleteAccount()
            }

            setNegativeButton("Não") { dialog, which ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun deleteAccount() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            it.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val loginIntent = Intent(this, LoginActivity::class.java)
                    loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(loginIntent)
                    finish()
                } else {
                    val errorMessage = task.exception?.message ?: "Erro ao excluir a conta."
                    showErrorDialog(errorMessage)
                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Erro")
            setMessage(message)
            setPositiveButton("OK", null)
            create()
            show()
        }
    }
}