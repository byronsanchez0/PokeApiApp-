package com.example.apppokeapi.data.mapper

import com.example.apppokeapi.data.local.PokemonEntity
import com.example.apppokeapi.domain.models.PokemonListItem

fun PokemonListItem.mapToEntitiy (): PokemonEntity{
    return PokemonEntity(
        name = this.name,
        image = getPokemonImage(url)
    )
}
fun PokemonEntity.mapToDomain(): PokemonListItem {
    return PokemonListItem(
        name = name,
        url = image
    )
}


private fun getPokemonImage(url: String): String {
    val pokemonId = extractNumberFromUrl(url)
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
}

fun extractNumberFromUrl(url: String): Int? {
    val regex = Regex("/(\\d+)/?$")
    val matchResult = regex.find(url)
    return matchResult?.groups?.get(1)?.value?.toIntOrNull()
}