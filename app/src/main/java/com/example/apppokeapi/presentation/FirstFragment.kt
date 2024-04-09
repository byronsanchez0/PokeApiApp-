package com.example.apppokeapi.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apppokeapi.databinding.FragmentFirstBinding
import com.example.apppokeapi.domain.models.PokemonListItem
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {
    private lateinit var binding: FragmentFirstBinding
    private val viewModel: PokemonListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println(viewModel)
        viewModel.getPokemonList()
        startObservable()
        val serviceIntent = Intent(this.context, MyBackgroundService::class.java)
        this.activity?.startService(serviceIntent)
    }


    private fun startObservable() {
        viewModel.pokemons().observe(viewLifecycleOwner) { pokemons ->

            setupRecyclerView(pokemons)
        }
        viewModel.loading().observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView(pokemons: List<PokemonListItem>) {
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerview.layoutManager = layoutManager
        val adapter = PokemonListAdapter(pokemons)
        binding.recyclerview.adapter = adapter

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "YOUR_ACTION_NAME") {
                val isConnected = isNetworkConnected(context)
                if(isConnected){
                    viewModel.getPokemonList()
                    view?.let { // Ensure the fragment's view is not null
                        Snackbar.make(it, "10 Pokemons han sido agregados", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    view?.let { // Ensure the fragment's view is not null
                        Snackbar.make(it, "No tienes conexion a internet, no se pueden cargar mas pokemons", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }
    }
    private fun isNetworkConnected(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetworkInfo?.isConnected ?: false
    }

    override fun onStart() {
        super.onStart()
        // Register the receiver
        val filter = IntentFilter("YOUR_ACTION_NAME")
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, filter)
    }

    override fun onStop() {
        super.onStop()
        // Unregister the receiver
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }
}