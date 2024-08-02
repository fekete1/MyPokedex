package br.com.fekete1.mypokedex.domain

import br.com.fekete1.mypokedex.api.model.Cries
import br.com.fekete1.mypokedex.api.model.PokemonAbility
import br.com.fekete1.mypokedex.api.model.PokemonStat
import br.com.fekete1.mypokedex.api.model.Shape
import com.google.gson.annotations.SerializedName

data class PokemonDetails(
    val number: Int,
    val name: String,
    val types: List<PokemonType>,
    val description: String? = null,
    val cries: Cries? = null,
    val height: String? = null,
    val weight: String? = null,
    val category: Shape? = null,
    val abilities: List<PokemonAbility>? = null,
    val stats: List<PokemonStat>? = null
) {
    val formattedName = name.capitalize()
    val formattedNumber = number.toString().padStart(3, '0')
    val imageUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/$formattedNumber.png"
}

data class Cries(
    val latest: String,
    val legacy: String
)

data class Shape(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)