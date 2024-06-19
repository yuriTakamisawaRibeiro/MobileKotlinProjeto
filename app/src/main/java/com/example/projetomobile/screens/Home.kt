package com.example.projetomobile.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Home(navController: NavHostController) {
    val firestore = FirebaseFirestore.getInstance()
    val productList = remember { mutableStateListOf<Product>() }

    LaunchedEffect(Unit) {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val product = document.toObject(Product::class.java)
                    product?.let { productList.add(it) }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao ler dados: $exception")
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(productList) { product ->
            ProductCard(name = product.name, description = product.description, price = product.price, )
        }
    }
}


@Composable
fun ProductCard(name: String, description: String, price: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "R$ $price", fontSize = 16.sp, color = Color.Gray)
        }
    }
}

data class Product(val name: String = "", val description: String="", val price: String="")
