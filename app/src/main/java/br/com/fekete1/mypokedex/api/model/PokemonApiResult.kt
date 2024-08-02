package br.com.fekete1.mypokedex.api.model

import br.com.fekete1.mypokedex.domain.PokemonType

data class PokemonsApiResult(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
)

data class PokemonApiResult(
    val id: Int,
    val name: String,
    val types: List<PokemonTypeSlot>,
)

data class PokemonDetailsApiResult(
    val id: Int,
    val name: String,
    val types: List<PokemonTypeSlot>,
    val cries: Cries,
    val height: String,
    val weight: String,
    val abilities: List<PokemonAbility>,
    val stats: List<PokemonStat>,
)

data class PokemonTypeSlot(
    val slot: Int,
    val type: PokemonType
)

data class Cries(
    val latest: String,
    val legacy: String
)

data class PokemonStat(
    val base_stat: Int?,
    val effort: Int,
    val stat: StatDetail,
)

data class StatDetail(
    val name: String,
    val url: String
)

data class PokemonAbility(
    val ability: AbilityDetail,
    val is_hidden: Boolean,
    val slot: Int
)

data class AbilityDetail(
    val name: String,
    val url: String
)