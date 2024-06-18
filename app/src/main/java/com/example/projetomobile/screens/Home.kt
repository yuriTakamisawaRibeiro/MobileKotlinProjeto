package com.example.projetomobile.screens

import android.view.LayoutInflater
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
<<<<<<< HEAD
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun Home(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

=======
import com.example.projetomobile.ui.theme.GreenJC
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ProductCard(name: String, description: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "R$ $value", fontSize = 16.sp, color = Color.Gray)
        }
    }
}

@Composable
fun Home() {
    val productList = listOf(
        Product("Product 1", "Description 1", "100.00"),
        Product("Product 2", "Description 2", "150.00"),
        // Add more products here...
    )

    LazyColumn {
        items(productList) { product ->
            ProductCard(name = product.name, description = product.description, value = product.value)
>>>>>>> 5f825cf360d49c076b0e821e6f75136c8c0f2f68
        }
    }
}

data class Product(val name: String, val description: String, val value: String)

