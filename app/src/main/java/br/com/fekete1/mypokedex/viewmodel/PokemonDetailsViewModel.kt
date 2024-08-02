package br.com.fekete1.mypokedex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fekete1.mypokedex.api.PokemonRepository
import br.com.fekete1.mypokedex.domain.PokemonDetails
import br.com.fekete1.mypokedex.domain.PokemonType
import br.com.fekete1.mypokedex.utils.getTypeColor
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(private val pokemonNumber: Int) : ViewModel() {

    private val _pokemon = MutableLiveData<PokemonDetails>()
    val pokemon: LiveData<PokemonDetails> get() = _pokemon

    init {
        loadPokemonDetails()
    }

    private fun loadPokemonDetails() {
        viewModelScope.launch {
            val pokemonResult = PokemonRepository.getPokemonDetails(pokemonNumber)
            val pokemonSpeciesResult = PokemonRepository.getPokemonSpecies(pokemonNumber)

            pokemonResult?.let { pokemonData ->
                val description = pokemonSpeciesResult?.flavorTextEntries
                    ?.firstOrNull { it.language.name == "en" }
                    ?.flavorText?.replace("\n", " ") ?: "Descrição não disponível"
                val category = pokemonSpeciesResult?.shape

                val pokemon = PokemonDetails(
                    number = pokemonData.id,
                    name = pokemonData.name,
                    types = pokemonData.types.map { type ->
                        PokemonType(name = type.type.name, color = getTypeColor(type.type.name))
                    },
                    description = description,
                    height = pokemonData.height,
                    weight = pokemonData.weight,
                    cries = pokemonData.cries,
                    category = category,
                    abilities = pokemonData.abilities,
                    stats = pokemonData.stats
                )
                _pokemon.postValue(pokemon)
            }
        }
    }
}
