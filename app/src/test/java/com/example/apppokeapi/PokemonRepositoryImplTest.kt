package com.example.apppokeapi

import com.example.apppokeapi.data.local.PokemonDao
import com.example.apppokeapi.data.local.PokemonEntity
import com.example.apppokeapi.data.mapper.mapToDomain
import com.example.apppokeapi.data.mapper.mapToEntitiy
import com.example.apppokeapi.data.remote.PokeApiService
import com.example.apppokeapi.data.repository.PokemonRepositoryImpl
import com.example.apppokeapi.domain.models.PokemonListItem
import com.example.apppokeapi.domain.models.PokemonListModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

class PokemonRepositoryImplTest {
    private lateinit var pokemonDao: PokemonDao
    private lateinit var pokeApiService: PokeApiService
    private lateinit var pokemonRepositoryImpl: PokemonRepositoryImpl

    @Before
    fun setUp() {
    pokemonDao = mock()
        pokeApiService = mock()
        pokemonRepositoryImpl = PokemonRepositoryImpl(pokemonDao, pokeApiService)
    }

    @Test
    fun `getPokemonList should return empty list on API error`() = runBlocking {
        // Arrange
        val offset = 0
        val limit = 15
        val response: Response<PokemonListModel> = mock()
        `when`(response.isSuccessful).thenReturn(false)
        `when`(pokeApiService.getPokemonList(offset, limit)).thenReturn(response)

        // Act
        val result = pokemonRepositoryImpl.getPokemonList(offset, limit)

        // Assert
        assertEquals(emptyList<PokemonListItem>(), result)
    }

    @Test
    fun `getPokemonList should return list of PokemonListItem on API success`() = runBlocking {
        val url = "https://pokemon.com/image"
        // Arrange
        val offset = 0
        val limit = 15
        val pokemonList = listOf(
            PokemonListItem("Bulbasaur", url),
            PokemonListItem("Charmander", url),
            PokemonListItem("Squirtle", url)
        )
        val pokemonListModel = PokemonListModel(950, "","", pokemonList)
        val response: Response<PokemonListModel> = mock()
        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.body()).thenReturn(pokemonListModel)
        `when`(pokeApiService.getPokemonList(offset, limit)).thenReturn(response)

        // Act
        val result = pokemonRepositoryImpl.getPokemonList(offset, limit)

        // Assert
        assertEquals(pokemonList, result)
    }

    @Test
    fun `insertPokemonListLocal should insert list of PokemonListItem into local database`() = runBlocking {
        val url = "https://pokemon.com/image"
        // Arrange
        val offset = 0
        val limit = 15
        val pokemonList = listOf(
            PokemonListItem("Bulbasaur", url),
            PokemonListItem("Charmander", url),
            PokemonListItem("Squirtle", url)
        )

        // Act
        pokemonRepositoryImpl.insertPokemonListLocal(pokemonList)

        // Assert
        verify(pokemonDao).insertAllPokemon(*pokemonList.map { it.mapToEntitiy() }.toTypedArray())
    }

    @Test
    fun `getAllPokemonFromLocal should return list of PokemonListItem from local database`() = runBlocking {
        // Arrange
        val url = "https://pokemon.com/image"
        val pokemonEntities = listOf(
            PokemonEntity("Bulbasaur", url),
            PokemonEntity("Charmander", url),
            PokemonEntity("Squirtle", url)
        )
        val expectedPokemonList = pokemonEntities.map { it.mapToDomain() }
        `when`(pokemonDao.getAllPokemon()).thenReturn(pokemonEntities)

        // Act
        val result = pokemonRepositoryImpl.getAllPokemonFromLocal()

        // Assert
        assertEquals(expectedPokemonList, result)
    }
}
