package br.com.fekete1.mypokedex.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fekete1.mypokedex.api.PokemonRepository
import br.com.fekete1.mypokedex.domain.Pokemon
import br.com.fekete1.mypokedex.api.model.PokemonsApiResult
import br.com.fekete1.mypokedex.domain.PokemonType
import br.com.fekete1.mypokedex.utils.getTypeColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonViewModel : ViewModel() {
    val pokemons = MutableLiveData<List<Pokemon?>>()
    val isLoadingInitial = MutableLiveData<Boolean>()
    private var offset = 0
    private val limit = 20
    var isLoading = false  // Flag para verificar se está carregando

    init {
        loadMorePokemons()
    }

    fun loadMorePokemons() {
        if (isLoading) return  // Não faça nada se já estiver carregando

        isLoading = true
        if (offset == 0) {
            isLoadingInitial.postValue(true)
        } else {
            pokemons.value = pokemons.value.orEmpty() + listOf(null) // Adiciona item de loading
        }

        viewModelScope.launch {
            try {
                Log.d("PokemonViewModel", "Loading more pokemons, offset: $offset")
                val pokemonsApiResult: PokemonsApiResult? = withContext(Dispatchers.IO) {
                    PokemonRepository.listPokemons(limit, offset)
                }
                val newPokemons = pokemonsApiResult?.results?.map { pokemonResult ->
                    val number = pokemonResult.url
                        .replace("https://pokeapi.co/api/v2/pokemon/", "")
                        .replace("/", "").toInt()

                    val pokemonApiResult = withContext(Dispatchers.IO) {
                        PokemonRepository.getPokemon(number)
                    }

                    pokemonApiResult?.let {
                        Pokemon(
                            pokemonApiResult.id,
                            pokemonApiResult.name,
                            pokemonApiResult.types.map { type ->
                                PokemonType(
                                    type.type.name,
                                    getTypeColor(type.type.name)
                                )
                            }
                        )
                    }
                }

                newPokemons?.let {
                    val currentList = pokemons.value.orEmpty().filterNotNull() // Remove item de loading
                    pokemons.postValue(currentList + it)
                    offset += limit
                }
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Exception fetching pokemon", e)
            } finally {
                isLoading = false
                isLoadingInitial.postValue(false)
            }
        }
    }
}