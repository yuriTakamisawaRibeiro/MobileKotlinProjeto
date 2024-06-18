package com.example.projetomobile.inflaters

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.projetomobile.R
import com.example.projetomobile.screens.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@Composable
fun AddPetXmlLayout(navController: NavHostController) {
    val context = LocalContext.current
    val storage = FirebaseStorage.getInstance()
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Estado para armazenar os dados do pet
    val nameState = remember { mutableStateOf("") }
    val breedState = remember { mutableStateOf("") }
    val colorState = remember { mutableStateOf("") }
    val sizeState = remember { mutableStateOf("") }
    val weightState = remember { mutableStateOf("") }
    val imageUriState = remember { mutableStateOf<Uri?>(null) }

    // Lançador para selecionar uma imagem da galeria
    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUriState.value = it
            // Atualizar a ImageView com a imagem selecionada
            val imageViewPet = (context as Activity).findViewById<ImageView>(R.id.imageViewPet)
            imageViewPet.setImageURI(it)
        }
    }

    AndroidView(factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.activity_add_pet, null, false)

        // EditText para raça, cor e porte
        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val editTextBreed = view.findViewById<EditText>(R.id.editTextBreed)
        val editTextColor = view.findViewById<EditText>(R.id.editTextColor)
        val editTextSize = view.findViewById<EditText>(R.id.editTextSize)
        val editTextWeight = view.findViewById<EditText>(R.id.editTextWeight)


        // ImageView para a imagem do pet
        val imageViewPet = view.findViewById<ImageView>(R.id.imageViewPet)

        // Botão para selecionar imagem
        val buttonUploadImage = view.findViewById<Button>(R.id.buttonUploadImage)
        buttonUploadImage.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        // Botão de cancelar
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        buttonCancel.setOnClickListener {
            navController.navigate(Screens.Home.screen) {
                popUpTo(Screens.PostPet.screen) { inclusive = true }
            }
        }

        // Botão de enviar
        val buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            // Recuperar os dados dos campos
            val name = editTextName.text.toString().trim()
            val breed = editTextBreed.text.toString().trim()
            val color = editTextColor.text.toString().trim()
            val size = editTextSize.text.toString().trim()
            val weight = editTextWeight.text.toString().trim()
            val imageUri = imageUriState.value

            // Validar campos
            if ( name.isEmpty() || breed.isEmpty() || color.isEmpty() || size.isEmpty() || weight.isEmpty() ) {
                showToast(context, "Por favor, preencha todos os campos.")
                return@setOnClickListener
            }

            // Validar porte
            if (size !in listOf("Pequeno", "Médio", "Grande")) {
                showToast(context, "Porte inválido. Deve ser Pequeno, Médio ou Grande.")
                return@setOnClickListener
            }

            // Validar imagem
            if (imageUri == null) {
                showToast(context, "Por favor, selecione uma imagem.")
                return@setOnClickListener
            }

            // Enviar dados para o Firebase
            uploadPetData(context, db, storage,name, breed, color, size, weight, imageUri, currentUser, navController)
        }

        view
    }, modifier = Modifier.fillMaxSize())
}

private fun uploadPetData(
    context: Context,
    db: FirebaseFirestore,
    storage: FirebaseStorage,
    name: String,
    breed: String,
    color: String,
    size: String,
    weight: String,
    imageUri: Uri,
    currentUser: FirebaseUser?,
    navController: NavHostController
) {
    val petId = UUID.randomUUID().toString()
    val storageRef = storage.reference.child("pets/$petId.jpg")

    storageRef.putFile(imageUri)
        .addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                val petData = hashMapOf(
                    "name" to name,
                    "breed" to breed,
                    "color" to color,
                    "size" to size,
                    "weight" to weight.toDouble(),
                    "imageUrl" to downloadUri.toString(),
                    "ownerId" to (currentUser?.uid ?: "")
                )

                db.collection("pets").document(petId).set(petData)
                    .addOnSuccessListener {
                        if (isActivityActive(context)) {
                            showToast(context, "Pet adicionado com sucesso!")
                            navController.navigate(Screens.Home.screen) {
                                popUpTo(Screens.PostPet.screen) { inclusive = true }
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        if (isActivityActive(context)) {
                            showToast(context, "Erro ao adicionar pet: ${e.message}")
                        }
                    }
            }.addOnFailureListener { e ->
                if (isActivityActive(context)) {
                    showToast(context, "Erro ao obter URL da imagem: ${e.message}")
                }
            }
        }
        .addOnFailureListener { e ->
            if (isActivityActive(context)) {
                showToast(context, "Erro ao fazer upload da imagem: ${e.message}")
            }
        }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

// Verifica se a activity está ativa e não está sendo destruída
private fun isActivityActive(context: Context): Boolean {
    return context is Activity && !context.isFinishing && !context.isDestroyed
}
