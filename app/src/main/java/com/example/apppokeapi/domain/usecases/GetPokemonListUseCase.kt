package com.example.apppokeapi.domain.usecases

import com.example.apppokeapi.domain.models.PokemonListItem
import com.example.apppokeapi.domain.repository.PokemonListRepoInterface
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonListRepoInterface
) {
    suspend operator fun invoke(): List<PokemonListItem> {
        if (repository.getAllPokemonFromLocal().isEmpty()) {
            val networkPokemonList = repository.getPokemonList(null, null)
            repository.insertPokemonListLocal(networkPokemonList)
        }
        return repository.getAllPokemonFromLocal()
    }
}