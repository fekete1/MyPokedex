package br.com.fekete1.mypokedex.api

import br.com.fekete1.mypokedex.api.model.PokemonApiResult
import br.com.fekete1.mypokedex.api.model.PokemonDetailsApiResult
import br.com.fekete1.mypokedex.api.model.PokemonSpeciesApiResult
import br.com.fekete1.mypokedex.api.model.PokemonsApiResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object PokemonRepository {

    private val service: PokemonService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(PokemonService::class.java)
    }

    suspend fun listPokemons(limit: Int, offset: Int): PokemonsApiResult? {
        return service.listPokemons(limit, offset)
    }

    suspend fun getPokemon(number: Int): PokemonApiResult? {
        return service.getPokemon(number)
    }

    suspend fun getPokemonSpecies(id: Int): PokemonSpeciesApiResult? {
        return service.getPokemonSpecies(id)
    }

    suspend fun getPokemonDetails(pokemonId: Int): PokemonDetailsApiResult? {
        return service.getPokemonDetails(pokemonId)
    }


    interface PokemonService {
        @GET("pokemon")
        suspend fun listPokemons(@Query("limit") limit: Int, @Query("offset") offset: Int): PokemonsApiResult

        @GET("pokemon/{number}")
        suspend fun getPokemon(@Path("number") number: Int): PokemonApiResult

        @GET("pokemon-species/{id}")
        suspend fun getPokemonSpecies(@Path("id") id: Int): PokemonSpeciesApiResult

        @GET("pokemon/{number}")
        suspend fun getPokemonDetails(@Path("number") number: Int): PokemonDetailsApiResult
    }
}