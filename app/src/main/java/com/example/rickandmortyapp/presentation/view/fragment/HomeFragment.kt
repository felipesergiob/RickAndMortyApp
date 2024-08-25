package com.example.rickandmortyapp.presentation.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentHomeBinding
import com.example.rickandmortyapp.presentation.view.adapter.CharacterAdapter
import com.example.rickandmortyapp.presentation.viewmodel.CharacterViewModel
import com.example.rickandmortyapp.presentation.viewmodel.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val characterViewModel: CharacterViewModel by viewModel()
    private val favoritesViewModel: FavoritesViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var newCharacterAdapter:CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Log do estado inicial dos favoritos
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            Log.d("HomeFragment", "Init: Current favorites: ${favorites.map { it.name }}")
        }

        binding.characterRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.starImageView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoritesFragment)
        }

        characterViewModel.charactersList.observe(viewLifecycleOwner) { characters ->
            favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
                val updatedCharacters = characters.map { character ->
                    character.copy(isFavorite = favorites.any { it.id == character.id })
                }
                newCharacterAdapter = CharacterAdapter(
                    updatedCharacters,
                    favoritesViewModel,
                    { character ->
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToDetailFragment(character.id)
                        findNavController().navigate(action)
                    }
                )
                binding.characterRecyclerView.adapter = newCharacterAdapter
            }
        }
        newCharacterAdapter.updateCharacterList(characterViewModel.charactersList)
        characterViewModel.fetchAllCharacters()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}