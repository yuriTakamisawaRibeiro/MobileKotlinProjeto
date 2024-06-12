package com.example.projetomobile.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetomobile.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

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
        } catch (e: Exception) {
            Log.e("SignupActivity", "Erro ao inicializar FirebaseAuth: ${e.message}", e)
            Toast.makeText(this, "Erro ao inicializar autenticação", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString().trim()
            val password = binding.signupPassword.text.toString().trim()
            val confirmPassword = binding.signupConfirm.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish() // Fecha a SignupActivity para não retornar ao cadastro
                            } else {
                                handleSignupError(task.exception)
                            }
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
}
