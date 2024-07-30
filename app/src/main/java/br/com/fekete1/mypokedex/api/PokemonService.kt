package br.com.fekete1.mypokedex.api

import br.com.fekete1.mypokedex.api.model.PokemonApiResult
import br.com.fekete1.mypokedex.api.model.PokemonsApiResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    fun listPokemons(@Query("limit") limit: Int,
                     @Query("offset") offset: Int): Call<PokemonsApiResult>

    @GET("pokemon/{number}")
    fun getPokemon(@Path("number") number: Int): Call<PokemonApiResult>
}
