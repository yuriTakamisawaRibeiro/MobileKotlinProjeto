package com.example.projetomobile.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.ui.theme.GreenJC
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.projetomobile.model.Pets
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Search(navController: NavHostController) {
    // Supondo que petsList seja a lista de pets que você quer mostrar
    val petsList = remember { mutableStateListOf<Pets>() }

    // Inicializa o Firestore
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        firestore.collection("pets") //
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val petData = document.toObject(Pets::class.java)
                    petData?.let { petsList.add(it) }
                }
            }
            .addOnFailureListener { exception ->
                // Trate o erro conforme necessário
                println("Erro ao buscar pets: $exception")
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Pets", fontSize = 30.sp, color = MaterialTheme.colorScheme.onSurface)


            LazyColumn {
                items(petsList) { pet ->
                    PetCard(pet)
                }
            }
        }
    }
}

@Composable
fun PetCard(pet: Pets) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = pet.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = pet.breed, fontSize = 16.sp)
            Text(text = pet.color, fontSize = 16.sp)
            Text(text = pet.size, fontSize = 16.sp)
            Text(text = pet.weight, fontSize = 16.sp)
        }
    }
}
