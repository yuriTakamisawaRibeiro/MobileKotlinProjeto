package com.example.projetomobile.screens

sealed class Screens (val screen : String) {
    data object Home : Screens("home")
    data object Search : Screens("search")
    data object Profile : Screens("profile")
    data object Delete : Screens("delete")
    data object PostProduct : Screens("postProduct")
    data object PostPet : Screens("postPet")
    data object PostReview : Screens("postReview")

}