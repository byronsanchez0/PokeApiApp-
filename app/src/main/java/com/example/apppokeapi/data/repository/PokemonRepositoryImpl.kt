package com.example.apppokeapi.data.repository

import android.util.Log
import com.example.apppokeapi.data.local.PokemonDao
import com.example.apppokeapi.data.mapper.mapToDomain
import com.example.apppokeapi.data.mapper.mapToEntitiy
import com.example.apppokeapi.data.remote.PokeApiService
import com.example.apppokeapi.domain.models.PokemonListItem
import com.example.apppokeapi.domain.repository.PokemonListRepoInterface
import javax.inject.Inject


class PokemonRepositoryImpl @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val pokemonApi: PokeApiService
) : PokemonListRepoInterface {
    override suspend fun getPokemonList(offset: Int?, limit: Int?): List<PokemonListItem> {
        return try {
            val response = pokemonApi.getPokemonList(offset = offset ?: 0, limit = limit ?: 15)
            if (response.isSuccessful) {
                response.body()?.results ?: run {
                    emptyList()
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.d("Test", e.toString())
            emptyList()
        }
    }

    override suspend fun insertPokemonListLocal(pokemonList: List<PokemonListItem>) {
        pokemonDao.insertAllPokemon(*pokemonList.map { it.mapToEntitiy() }.toTypedArray())
    }

    override suspend fun getAllPokemonFromLocal(): List<PokemonListItem> {
        return pokemonDao.getAllPokemon().map { it.mapToDomain() }.toList()
    }
}