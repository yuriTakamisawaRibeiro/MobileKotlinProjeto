package com.example.projetomobile.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetomobile.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateUserDataActivity : AppCompatActivity() {
    private lateinit var newNameEditText: EditText
    private lateinit var newPhoneEditText: EditText
    private lateinit var newEmailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var backButton: Button
    private lateinit var confirmButton: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_data)

        newNameEditText = findViewById(R.id.editTextNewName)
        newPhoneEditText = findViewById(R.id.editTextNewPhone)
        newEmailEditText = findViewById(R.id.editTextNewEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        backButton = findViewById(R.id.buttonBack)
        confirmButton = findViewById(R.id.buttonConfirm)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        newPhoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        // Recuperar dados atuais do usuário e preencher os campos
        fillCurrentUserData()

        backButton.setOnClickListener {
            finish() // Voltar à atividade anterior
        }

        confirmButton.setOnClickListener {
            val name = newNameEditText.text.toString().trim()
            val phone = newPhoneEditText.text.toString().trim()
            val newEmail = newEmailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || newEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            } else if (!isValidPhoneNumber(phone)) {
                Toast.makeText(this, "Por favor, insira um telefone válido.", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(newEmail)) {
                Toast.makeText(this, "Por favor, insira um e-mail válido.", Toast.LENGTH_SHORT).show()
            } else {
                reauthenticateAndUpdateData(name, phone, newEmail, password)
            }
        }
    }

    private fun fillCurrentUserData() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            // Preencher o e-mail atual
            newEmailEditText.setText(user.email)

            // Recuperar dados do Firestore
            val userId = user.uid
            val userDocument = firestore.collection("users").document(userId)

            userDocument.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Preencher o nome e telefone atuais se existirem
                    val currentName = document.getString("name")
                    val currentPhone = document.getString("phone")

                    newNameEditText.setText(currentName ?: "")
                    newPhoneEditText.setText(currentPhone ?: "")
                } else {
                    Log.w("UpdateUserDataActivity", "Nenhum documento encontrado para o usuário.")
                }
            }.addOnFailureListener { e ->
                Log.e("UpdateUserDataActivity", "Erro ao recuperar dados do Firestore: ${e.message}", e)
                Toast.makeText(this, "Erro ao carregar dados do usuário.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reauthenticateAndUpdateData(name: String, phone: String, newEmail: String, password: String) {
        val user = firebaseAuth.currentUser

        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, password)

            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    // Verificar se o novo e-mail já está em uso
                    firebaseAuth.fetchSignInMethodsForEmail(newEmail).addOnCompleteListener { fetchTask ->
                        if (fetchTask.isSuccessful) {
                            val signInMethods = fetchTask.result?.signInMethods
                            if (signInMethods.isNullOrEmpty() || signInMethods.contains("password")) {
                                user.updateEmail(newEmail).addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        updateUserInFirestore(name, phone, newEmail)
                                    } else {
                                        showError("Falha ao atualizar e-mail", updateTask.exception)
                                    }
                                }
                            } else {
                                Toast.makeText(this, "O e-mail já está em uso por outro usuário.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            showError("Falha ao verificar disponibilidade do e-mail", fetchTask.exception)
                        }
                    }
                } else {
                    showError("Falha na reautenticação. Verifique sua senha.", reauthTask.exception)
                }
            }
        } else {
            Toast.makeText(this, "Nenhum usuário autenticado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserInFirestore(name: String, phone: String, newEmail: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val userMap = hashMapOf(
            "name" to name,
            "phone" to phone,
            "email" to newEmail
        )
        firestore.collection("users").document(userId)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Dados atualizados com sucesso.", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent()
                resultIntent.putExtra("updated_email", newEmail)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener { e ->
                showError("Erro ao salvar dados no Firestore", e)
            }
    }

    private fun showError(message: String, exception: Exception?) {
        val errorMessage = exception?.message ?: "Erro desconhecido"
        Log.e("UpdateUserDataActivity", "$message: $errorMessage", exception)
        Toast.makeText(this, "$message: $errorMessage", Toast.LENGTH_SHORT).show()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val regex = Regex("\\(\\d{2}\\) \\d{4,5}-\\d{4}")
        return regex.matches(phone)
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private class PhoneNumberFormattingTextWatcher : TextWatcher {
        private var isFormatting = false
        private var deletingBackward = false

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            deletingBackward = count == 1 && after == 0
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (isFormatting || s == null) return

            isFormatting = true

            val phone = s.toString().filter { it.isDigit() }
            val formatted = when {
                phone.length <= 2 -> phone
                phone.length <= 7 -> "(${phone.substring(0, 2)}) ${phone.substring(2)}"
                phone.length <= 11 -> "(${phone.substring(0, 2)}) ${phone.substring(2, 7)}-${phone.substring(7)}"
                else -> "(${phone.substring(0, 2)}) ${phone.substring(2, 7)}-${phone.substring(7, 11)}"
            }

            s.replace(0, s.length, formatted)

            isFormatting = false
        }
    }
}
