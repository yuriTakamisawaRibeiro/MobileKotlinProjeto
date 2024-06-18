package com.example.projetomobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Home(navController: NavHostController) {
    val productList = listOf(
        Product("Product 1", "Description 1", "100.00"),
        Product("Product 2", "Description 2", "150.00")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(productList) { product ->
            ProductCard(name = product.name, description = product.description, value = product.value)
        }
    }
}

@Composable
fun ProductCard(name: String, description: String, value: String) {
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
            Text(text = "R$ $value", fontSize = 16.sp, color = Color.Gray)
        }
    }
}

data class Product(val name: String, val description: String, val value: String)
