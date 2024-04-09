package com.example.apppokeapi.domain.repository

import com.example.apppokeapi.domain.models.PokemonListItem
import com.example.apppokeapi.domain.models.PokemonListModel
import retrofit2.Response

interface PokemonListRepoInterface {
    suspend fun getPokemonList(offset: Int?, limit: Int?): List<PokemonListItem>
    suspend fun insertPokemonListLocal(pokemonList: List<PokemonListItem>)
    suspend fun getAllPokemonFromLocal():List<PokemonListItem>

}
