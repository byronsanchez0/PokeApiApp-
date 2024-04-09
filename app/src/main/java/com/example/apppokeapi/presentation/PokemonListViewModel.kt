package com.example.apppokeapi.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppokeapi.domain.models.PokemonListItem
import com.example.apppokeapi.domain.usecases.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val useCase: GetPokemonListUseCase
) : ViewModel() {
    private val pokemonsMutableLiveData = MutableLiveData<List<PokemonListItem>>()
    fun pokemons(): LiveData<List<PokemonListItem>> = pokemonsMutableLiveData

    private val loadingMutableLiveData = MutableLiveData<Boolean>(false)
    fun loading(): LiveData<Boolean> = loadingMutableLiveData

    fun getPokemonList() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingMutableLiveData.postValue(true)
            val pokemonList = useCase()
            loadingMutableLiveData.postValue(false)
            pokemonsMutableLiveData.postValue(pokemonList)
        }
    }
}