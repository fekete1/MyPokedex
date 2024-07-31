package br.com.fekete1.mypokedex.utils

import br.com.fekete1.mypokedex.R

fun getTypeColor(type: String): Int {
    return when (type.toLowerCase()) {
        "grass" -> R.color.grass
        "fire" -> R.color.fire
        "water" -> R.color.water
        "bug" -> R.color.bug
        "normal" -> R.color.normal
        "poison" -> R.color.poison
        "electric" -> R.color.electric
        "ground" -> R.color.ground
        "fairy" -> R.color.fairy
        "fighting" -> R.color.fighting
        "psychic" -> R.color.psychic
        "rock" -> R.color.rock
        "ghost" -> R.color.ghost
        "ice" -> R.color.ice
        "dragon" -> R.color.dragon
        "dark" -> R.color.dark
        "steel" -> R.color.steel
        "flying" -> R.color.flying
        else -> R.color.normal // default color
    }
}