package com.example.projetomobile

sealed class Screens (val screen : String) {
    data object Home : Screens("home")
    data object Search : Screens("search")
    data object Profile : Screens("profile")
    data object Delete : Screens("delete")


}