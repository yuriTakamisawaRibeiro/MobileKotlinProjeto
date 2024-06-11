package com.example.projetomobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projetomobile.ui.theme.GreenJC
import com.example.projetomobile.ui.theme.ProjetomobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetomobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyBottomAppBar()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomAppBar() {
    val navigationController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }  // Usando `by` para delegar corretamente o `MutableState`

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = GreenJC
            ) {
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Home
                        navigationController.navigate(Screens.Home.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Home) Color.White else Color.DarkGray
                    )
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Search
                        navigationController.navigate(Screens.Search.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Search) Color.White else Color.DarkGray
                    )
                }

                Box(modifier = Modifier
                    .weight(1f)
                    .padding(16.dp), contentAlignment = Alignment.Center) {
                    FloatingActionButton(onClick = { showBottomSheet = true }) { // Corrigido: showBottomSheet = true
                        Icon(Icons.Default.Add, contentDescription = null, tint = GreenJC)
                    }
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Delete
                        navigationController.navigate(Screens.Delete.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Delete) Color.White else Color.DarkGray
                    )
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Person
                        navigationController.navigate(Screens.Profile.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Person) Color.White else Color.DarkGray
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(navController = navigationController, startDestination = Screens.Home.screen, modifier = Modifier.padding(paddingValues)) {
            composable(Screens.Home.screen) { Home() }
            composable(Screens.Search.screen) { Search() }
            composable(Screens.Delete.screen) { Delete() }
            composable(Screens.Profile.screen) { Profile() }

            composable(Screens.PostProduct.screen) { PostProduct() }
            composable(Screens.PostPet.screen) { PostPet() }
            composable(Screens.PostReview.screen) { PostReview() }

        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },  // Correção: Usando `showBottomSheet = false` para fechar o Bottom Sheet
            sheetState = sheetState  // Corrigido: Passando `sheetState` corretamente
        ) {
            // Conteúdo do seu Bottom Sheet aqui
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                BottomSheetItem(icon = Icons.Default.ShoppingCart, title = "Adicione um produto") {
                    showBottomSheet = false
                    navigationController.navigate(Screens.PostProduct.screen) {
                        popUpTo(0)
                    }
                }

                BottomSheetItem(icon = Icons.Default.Add, title = "Adicione um pet") {
                    showBottomSheet = false
                    navigationController.navigate(Screens.PostPet.screen) {
                        popUpTo(0)
                    }
                }

                BottomSheetItem(icon = Icons.Default.ThumbUp, title = "Adicione uma review") {
                    showBottomSheet = false
                    navigationController.navigate(Screens.PostReview.screen) {
                        popUpTo(0)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp) // Adicionando padding para toque mais fácil e espaçamento apropriado
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = GreenJC,
            modifier = Modifier.size(24.dp) // Ajuste de tamanho do ícone, se necessário
        )
        Text(
            text = title,
            color = GreenJC,
            fontSize = 22.sp // Tamanho de fonte apropriado
        )
    }
}