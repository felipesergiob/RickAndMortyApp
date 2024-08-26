package com.example.rickandmortyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyapp.databinding.FragmentHomeBinding
import com.example.rickandmortyapp.viewmodel.CharacterViewModel
import com.example.rickandmortyapp.viewmodel.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private val characterViewModel: CharacterViewModel by sharedViewModel()
    private val favoritesViewModel: FavoritesViewModel by sharedViewModel()
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
            findNavController().navigate(com.example.rickandmortyapp.R.id.action_homeFragment_to_favoritesFragment)
        }

        characterViewModel.charactersList.observe(viewLifecycleOwner) { characters ->
            binding.characterRecyclerView.adapter = CharacterAdapter(
                characters,
                favoritesViewModel,
                onItemClick = { character ->
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(character)
                    findNavController().navigate(action)
                },
                allowFavoriteToggle = false
            )
        }

        characterViewModel.fetchAllCharacters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
