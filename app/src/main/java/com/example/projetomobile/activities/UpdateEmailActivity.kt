package com.example.projetomobile.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetomobile.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class UpdateEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_email)

        val newEmailEditText = findViewById<EditText>(R.id.editTextNewEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val backButton = findViewById<Button>(R.id.buttonBack)
        val confirmButton = findViewById<Button>(R.id.buttonConfirm)

        backButton.setOnClickListener {
            finish() // Voltar à atividade anterior
        }

        confirmButton.setOnClickListener {
            val newEmail = newEmailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validar o e-mail
            if (newEmail.isEmpty()) {
                Toast.makeText(this, "Por favor, insira um novo e-mail.", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                Toast.makeText(this, "Por favor, insira um e-mail válido.", Toast.LENGTH_SHORT)
                    .show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Por favor, insira sua senha atual.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Chama a função para reautenticar e atualizar o e-mail
                    reauthenticateAndUpdateEmail(newEmail, password)
            }
        }
    }

    // Agora a função updateEmail está dentro da classe UpdateEmailActivity
    private fun reauthenticateAndUpdateEmail(newEmail: String, password: String) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user!= null && user.email!= null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, password)

            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    // Verificar se o novo e-mail já está em uso
                    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(newEmail).addOnCompleteListener { fetchTask ->
                        if (fetchTask.isSuccessful) {
                            val signInMethods = fetchTask.result.signInMethods
                            if (signInMethods.isNullOrEmpty() || signInMethods.contains("password")) {
                                // O e-mail não está em uso ou é o mesmo do usuário atual
                                user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener { verifyTask ->
                                    if (verifyTask.isSuccessful) {
                                        // Se a verificação for bem-sucedida, atualizar o e-mail
                                        user.updateEmail(newEmail).addOnCompleteListener { updateTask ->
                                            if (updateTask.isSuccessful) {
                                                Toast.makeText(this, "E-mail atualizado com sucesso.", Toast.LENGTH_SHORT).show()
                                                val resultIntent = Intent()
                                                resultIntent.putExtra("updated_email", newEmail)
                                                setResult(Activity.RESULT_OK, resultIntent)
                                                finish()
                                            } else {
                                                // Tratar possíveis erros na atualização do e-mail
                                                val errorMessage = updateTask.exception?.message?: "Erro desconhecido"
                                                Toast.makeText(this, "Falha ao atualizar e-mail: $errorMessage", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        // Tratar falhas na verificação do novo e-mail
                                        val errorMessage = verifyTask.exception?.message?: "Erro ao verificar novo e-mail."
                                        Toast.makeText(this, "Falha na verificação do novo e-mail: $errorMessage", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                // O e-mail já está em uso por outro usuário
                                Toast.makeText(this, "O e-mail já está em uso por outro usuário.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Tratar erros na busca de métodos de login
                            val errorMessage = fetchTask.exception?.message?: "Erro ao verificar disponibilidade do e-mail."
                            Toast.makeText(this, "Falha ao verificar disponibilidade do e-mail: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Tratar erros na reautenticação
                    val errorMessage = reauthTask.exception?.message?: "Erro ao reautenticar. Verifique sua senha."
                    Toast.makeText(this, "Falha na reautenticação: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Nenhum usuário autenticado.", Toast.LENGTH_SHORT).show()
        }
    }


}