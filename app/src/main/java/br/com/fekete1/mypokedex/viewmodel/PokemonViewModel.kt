import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fekete1.mypokedex.api.PokemonRepository
import br.com.fekete1.mypokedex.domain.Pokemon
import br.com.fekete1.mypokedex.api.model.PokemonsApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonViewModel : ViewModel() {
    val pokemons = MutableLiveData<List<Pokemon?>>()
    private var offset = 0
    private val limit = 20
    var isLoading = false  // Flag para verificar se está carregando

    init {
        loadMorePokemons()
    }

    fun loadMorePokemons() {
        if (isLoading) return  // Não faça nada se já estiver carregando

        isLoading = true
        viewModelScope.launch {
            try {
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
                                type.type
                            }
                        )
                    }
                }

                newPokemons?.let {
                    val currentList = pokemons.value.orEmpty()
                    pokemons.postValue(currentList + it)
                    offset += limit
                }
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Exception fetching pokemon", e)
            } finally {
                isLoading = false
            }
        }
    }
}