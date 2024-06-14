package com.example.projetomobile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.ui.theme.GreenJC
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore

@Composable
fun PostProduct() {
    val productName = remember { mutableStateOf("") }
    val productDescription = remember { mutableStateOf("") }
    val productPrice = remember { mutableStateOf("") }
    val db = Firebase.firestore

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Cadastrar Produto", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = productName.value,
            onValueChange = { productName.value = it },
            label = { Text("Nome do Produto") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = productDescription.value,
            onValueChange = { productDescription.value = it },
            label = { Text("Descrição do Produto") },
            singleLine = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = productPrice.value,
            onValueChange = { productPrice.value = it },
            label = { Text("Preço do Produto") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val product = hashMapOf(
                "name" to productName.value,
                "description" to productDescription.value,
                "price" to productPrice.value
            )
            db.collection("products")
                .add(product)
                .addOnSuccessListener { documentReference ->
                    println("Produto salvo com sucesso com ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    println("Falha ao salvar o produto: $e")
                }
        }) {
            Text("Salvar Produto")
        }
    }
}