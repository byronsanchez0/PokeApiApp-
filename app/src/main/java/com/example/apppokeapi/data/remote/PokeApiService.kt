package com.example.apppokeapi.data.remote

import com.example.apppokeapi.domain.models.PokemonDetailsModel
import com.example.apppokeapi.domain.models.PokemonListModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ):Response<PokemonListModel>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") name: String
    ): Response<PokemonDetailsModel>
}