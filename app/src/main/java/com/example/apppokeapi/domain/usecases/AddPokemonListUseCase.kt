package com.example.apppokeapi.domain.usecases

import com.example.apppokeapi.domain.repository.PokemonListRepoInterface
import javax.inject.Inject

class AddPokemonListUseCase @Inject constructor(
    private val repository: PokemonListRepoInterface
) {
    suspend operator fun invoke() {
        if (repository.getAllPokemonFromLocal().isNotEmpty()) {
            val loadedPokemons = repository.getAllPokemonFromLocal()
            val networkPokemonList = repository.getPokemonList(loadedPokemons.size, 10)
            repository.insertPokemonListLocal(networkPokemonList)
        }
    }
}