package com.example.apppokeapi.di

import android.content.Context
import androidx.room.Room
import com.example.apppokeapi.data.datautil.Constants
import com.example.apppokeapi.data.local.PokemonDao
import com.example.apppokeapi.data.local.PokemonDatabase
import com.example.apppokeapi.data.remote.PokeApiService
import com.example.apppokeapi.data.repository.PokemonRepositoryImpl
import com.example.apppokeapi.domain.repository.PokemonListRepoInterface
import com.example.apppokeapi.domain.usecases.GetPokemonListUseCase
import com.example.apppokeapi.presentation.PokemonListViewModel
import dagger.Component.Factory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        pokemonDao: PokemonDao,
        pokeApiService: PokeApiService
    ): PokemonListRepoInterface = PokemonRepositoryImpl(
        pokemonDao = pokemonDao,
        pokemonApi = pokeApiService
    )

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providePokemonApi(
        client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): PokeApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideGetPokemonListUseCase(
        pokemonRepo: PokemonListRepoInterface
    ): GetPokemonListUseCase {
        return GetPokemonListUseCase(pokemonRepo)
    }

    @Provides
    @Singleton
    fun providePokemonDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, PokemonDatabase::class.java, POKEMON_DATABASE).build()

    @Provides
    @Singleton
    fun providePokemonDao(db: PokemonDatabase) = db.pokemonDao()
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun providesVieModel(getPokemonListUseCase: GetPokemonListUseCase) : PokemonListViewModel {
        return PokemonListViewModel(getPokemonListUseCase)
    }
}
const val POKEMON_DATABASE = "pokemon_database"
