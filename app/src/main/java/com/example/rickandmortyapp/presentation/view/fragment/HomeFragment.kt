package com.example.rickandmortyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyapp.databinding.FragmentHomeBinding
import com.example.rickandmortyapp.viewmodel.FavoritesViewModel
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.viewmodel.CharacterViewModel

class HomeFragment : Fragment() {

    private val characterViewModel: CharacterViewModel by activityViewModels()
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.characterRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.starImageView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoritesFragment)
        }

        characterViewModel.charactersList.observe(viewLifecycleOwner) { characters ->
            binding.characterRecyclerView.adapter = CharacterAdapter(characters, favoritesViewModel, { character ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(character.id.toInt())
                findNavController().navigate(action)
            }, allowFavoriteToggle = false)
        }


        characterViewModel.fetchAllCharacters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
