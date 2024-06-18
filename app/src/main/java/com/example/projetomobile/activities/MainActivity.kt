package com.example.projetomobile.activities

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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projetomobile.screens.Delete
import com.example.projetomobile.screens.Home
import com.example.projetomobile.screens.PostPet
import com.example.projetomobile.screens.PostProduct
import com.example.projetomobile.screens.PostReview
import com.example.projetomobile.screens.Profile
import com.example.projetomobile.screens.Screens
import com.example.projetomobile.screens.Search
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
                    val navController = rememberNavController() as NavHostController
                    MyBottomAppBar(navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomAppBar(navController: NavHostController) {
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = GreenJC
            ) {
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Home
                        navController.navigate(Screens.Home.screen) {
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
                        navController.navigate(Screens.Search.screen) {
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
                    FloatingActionButton(onClick = { showBottomSheet = true }) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = GreenJC)
                    }
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Delete
                        navController.navigate(Screens.Delete.screen) {
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
                        navController.navigate(Screens.Profile.screen) {
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
        NavHost(navController = navController, startDestination = Screens.Home.screen, modifier = Modifier.padding(paddingValues)) {
            composable(Screens.Home.screen) { Home(navController) }
            composable(Screens.Search.screen) { Search(navController) }
            composable(Screens.Delete.screen) { Delete(navController) }
            composable(Screens.Profile.screen) { Profile(navController) }

            composable(Screens.PostProduct.screen) { PostProduct(navController) }
            composable(Screens.PostPet.screen) { PostPet(navController) }
            composable(Screens.PostReview.screen) { PostReview(navController) }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                BottomSheetItem(icon = Icons.Default.ShoppingCart, title = "Adicione um produto") {
                    showBottomSheet = false
                    navController.navigate(Screens.PostProduct.screen) {
                        popUpTo(0)
                    }
                }

                BottomSheetItem(icon = Icons.Default.Add, title = "Adicione um pet") {
                    showBottomSheet = false
                    navController.navigate(Screens.PostPet.screen) {
                        popUpTo(0)
                    }
                }

                BottomSheetItem(icon = Icons.Default.ThumbUp, title = "Adicione uma review") {
                    showBottomSheet = false
                    navController.navigate(Screens.PostReview.screen) {
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
            .padding(16.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = GreenJC,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            color = GreenJC,
            fontSize = 22.sp
        )
    }
}