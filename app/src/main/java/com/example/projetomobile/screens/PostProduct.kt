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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.projetomobile.ui.theme.GreenJC
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
<<<<<<< HEAD
fun PostProduct(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "PostProduct", fontSize = 30.sp, color = GreenJC)
=======
fun PostProduct() {
    val productName = remember { mutableStateOf("") }
    val productDescription = remember { mutableStateOf("") }
    val productPrice = remember { mutableStateOf("") }
    val productQuantity = remember { mutableStateOf("") }
    val productFunctionality = remember { mutableStateOf("") }

    val db = Firebase.firestore

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Cadastrar Produto", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = productName.value,
            onValueChange = { productName.value = it },
            label = { Text("Nome do Produto") },
            singleLine = true,
            isError = productName.value.isBlank(), // Validação: campo não pode estar vazio
            placeholder = { Text("Digite o nome do produto") } // Mensagem de erro
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = productFunctionality.value,
            onValueChange = { productFunctionality.value = it },
            label = { Text("Funcionalidade") },
            singleLine = false,
            isError = productFunctionality.value.isBlank(),
            placeholder = { Text("Descreva a funcionalidade do produto") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = productDescription.value,
            onValueChange = { productDescription.value = it },
            label = { Text("Descrição do Produto") },
            singleLine = false,
            isError = productDescription.value.isBlank(),
            placeholder = { Text("Digite a descrição do produto") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = productPrice.value,
            onValueChange = { productPrice.value = it },
            label = { Text("Preço do Produto") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = productPrice.value.isBlank(),
            placeholder = { Text("Digite o preço do produto") },
            leadingIcon = {
                Text("R$")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = productQuantity.value,
            onValueChange = { productQuantity.value = it },
            label = { Text("Quantidade") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = productQuantity.value.isBlank(),
            placeholder = { Text("Digite a quantidade disponível") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (productName.value.isNotBlank() &&
                    productFunctionality.value.isNotBlank() &&
                    productDescription.value.isNotBlank() &&
                    productPrice.value.isNotBlank() &&
                    productQuantity.value.isNotBlank()
                ) {
                    val product = hashMapOf(
                        "name" to productName.value,
                        "description" to productDescription.value,
                        "price" to productPrice.value,
                        "quantity" to productQuantity.value,
                        "functionality" to productFunctionality.value
                    )
                    db.collection("products")
                        .add(product)
                        .addOnSuccessListener { documentReference ->
                            println("Produto salvo com sucesso com ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            println("Falha ao salvar o produto: $e")
                        }
                } else {
                    println("Preencha todos os campos corretamente.")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
        ) {
            Text("Salvar Produto")
>>>>>>> 5f825cf360d49c076b0e821e6f75136c8c0f2f68
        }
    }
}

