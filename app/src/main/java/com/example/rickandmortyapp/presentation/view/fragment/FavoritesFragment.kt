package com.example.rickandmortyapp.presentation.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapp.databinding.FragmentFavoritesBinding
import com.example.rickandmortyapp.view.CharacterAdapter
import com.example.rickandmortyapp.viewmodel.FavoritesViewModel
import com.example.rickandmortyapp.domain.model.Character // Certifique-se de que esta é a classe correta!

class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        favoritesViewModel.loadFavorites()
        Log.i("FavoritesFragment", "Chamado")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            binding.favoritesRecyclerView.adapter = CharacterAdapter(
                favorites as List<com.example.rickandmortyapp.domain.model.Character>, // Certifique-se de que esta conversão está utilizando a classe correta
                favoritesViewModel,
                allowFavoriteToggle = false,
                isFavoriteView = true
            )
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
