package br.com.fekete1.mypokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PokemonDetailsViewModelFactory(private val pokemonNumber: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonDetailsViewModel(pokemonNumber) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}