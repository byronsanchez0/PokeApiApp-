package com.example.apppokeapi.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apppokeapi.R
import com.example.apppokeapi.databinding.PokeitemBinding
import com.example.apppokeapi.domain.models.PokemonListItem


class PokemonListAdapter(
    private val pokemons: List<PokemonListItem>
) : RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>() {
    inner class PokemonViewHolder(binding: PokeitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val pokemonName: TextView = binding.pokeName
        val pokemonImg: ImageView = binding.pokeImg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = PokeitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.pokemonName.text = pokemon.name
        Glide.with(holder.itemView.rootView.context)
            .load(pokemon.url)
            .placeholder(R.drawable.pokeball)
            .into(holder.pokemonImg)
    }
    //val title: TextView
}