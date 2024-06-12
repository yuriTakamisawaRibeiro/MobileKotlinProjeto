package com.example.projetomobile
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetomobile.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Exception) {
            Log.e("LoginActivity", "Erro ao inflar o layout: ${e.message}", e)
            Toast.makeText(this, "Erro ao carregar a tela de login", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        try {
            firebaseAuth = FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.e("LoginActivity", "Erro ao inicializar o FirebaseAuth: ${e.message}", e)
            Toast.makeText(this, "Erro ao inicializar autenticação", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("LoginActivity", "Erro ao fazer login: ${it.exception?.message}", it.exception)
                        Toast.makeText(this, it.exception?.message ?: "Erro desconhecido", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Campos não podem estar vazios", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupRedirectText.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)
        }
    }
}