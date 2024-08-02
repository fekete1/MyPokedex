package br.com.fekete1.mypokedex.api.model

import com.google.gson.annotations.SerializedName

data class PokemonSpeciesApiResult(
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>,
    @SerializedName("shape") val shape: Shape
)

data class FlavorTextEntry(
    @SerializedName("flavor_text") val flavorText: String,
    @SerializedName("language") val language: Language
)

data class Language(
    @SerializedName("name") val name: String
)

data class Shape(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)