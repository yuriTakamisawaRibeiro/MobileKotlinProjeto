package com.example.projetomobile.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetomobile.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivitySignupBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Exception) {
            Log.e("SignupActivity", "Erro ao inflar o layout: ${e.message}", e)
            Toast.makeText(this, "Erro ao carregar a tela de cadastro", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        try {
            firebaseAuth = FirebaseAuth.getInstance()
            firestore = FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.e("SignupActivity", "Erro ao inicializar FirebaseAuth ou Firestore: ${e.message}", e)
            Toast.makeText(this, "Erro ao inicializar autenticação", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.signupPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.signupButton.setOnClickListener {
            val name = binding.signupName.text.toString().trim()
            val phone = binding.signupPhone.text.toString().trim()
            val email = binding.signupEmail.text.toString().trim()
            val password = binding.signupPassword.text.toString().trim()
            val confirmPassword = binding.signupConfirm.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    if (isValidPhoneNumber(phone)) {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = firebaseAuth.currentUser
                                    user?.let {
                                        val userId = it.uid
                                        val userMap = hashMapOf(
                                            "name" to name,
                                            "phone" to phone,
                                            "email" to email
                                        )
                                        firestore.collection("users").document(userId)
                                            .set(userMap)
                                            .addOnSuccessListener {
                                                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                                                val intent = Intent(this, LoginActivity::class.java)
                                                startActivity(intent)
                                                finish() // Fecha a SignupActivity para não retornar ao cadastro
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("SignupActivity", "Erro ao salvar dados do usuário no Firestore: ${e.message}", e)
                                                Toast.makeText(this, "Erro ao salvar dados adicionais", Toast.LENGTH_LONG).show()
                                            }
                                    }
                                } else {
                                    handleSignupError(task.exception)
                                }
                            }
                    } else {
                        Toast.makeText(this, "Número de telefone inválido", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish() // Fecha a SignupActivity para não retornar ao cadastro
        }
    }

    private fun handleSignupError(exception: Exception?) {
        if (exception != null) {
            when (exception) {
                is FirebaseAuthWeakPasswordException -> {
                    Toast.makeText(this, "Senha fraca: ${exception.reason}", Toast.LENGTH_LONG).show()
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    Toast.makeText(this, "Email inválido", Toast.LENGTH_LONG).show()
                }
                is FirebaseAuthUserCollisionException -> {
                    Toast.makeText(this, "Este email já está em uso", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "Erro ao cadastrar: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
            Log.e("SignupActivity", "Erro ao cadastrar: ${exception.message}", exception)
        }
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val regex = Regex("\\(\\d{2}\\) \\d{5}-\\d{4}")
        return regex.matches(phone)
    }

    private class PhoneNumberFormattingTextWatcher : TextWatcher {

        private var isFormatting: Boolean = false
        private var deletingBackward: Boolean = false

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            if (count == 1 && after == 0) {
                deletingBackward = true
            } else {
                deletingBackward = false
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (isFormatting) return

            isFormatting = true

            val phone = s.toString().filter { it.isDigit() }
            val formatted = when {
                phone.length <= 2 -> phone
                phone.length <= 7 -> "(${phone.substring(0, 2)}) ${phone.substring(2)}"
                phone.length <= 11 -> "(${phone.substring(0, 2)}) ${phone.substring(2, 7)}-${phone.substring(7)}"
                else -> "(${phone.substring(0, 2)}) ${phone.substring(2, 7)}-${phone.substring(7, 11)}"
            }

            s?.replace(0, s.length, formatted)

            isFormatting = false
        }
    }
}
