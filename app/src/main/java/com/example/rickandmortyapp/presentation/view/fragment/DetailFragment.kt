package com.example.rickandmortyapp.presentation.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentDetailBinding
import com.example.rickandmortyapp.presentation.viewmodel.CharacterViewModel
import com.example.rickandmortyapp.presentation.viewmodel.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.rickandmortyapp.domain.model.Character


class DetailFragment : Fragment() {

    private val characterViewModel: CharacterViewModel by viewModel() // Injeção via Koin
    private val favoritesViewModel: FavoritesViewModel by viewModel() // Injeção via Koin
    private val args: DetailFragmentArgs by navArgs()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterViewModel.characterData.observe(viewLifecycleOwner) { character ->
            character?.let {
                binding.nameTextView.text = character.name
                binding.detailsTextView.text = """
                Gender: ${character.gender}
                Species: ${character.species}
                Status: ${character.status}
                Created: ${character.created}
            """.trimIndent()

                Glide.with(this)
                    .load(character.image)
                    .into(binding.characterImageView)

                updateStarIcon(character)

                binding.starImageView.setOnClickListener {
                    if (favoritesViewModel.isFavorite(character)) {
                        favoritesViewModel.removeFromFavorites(character)
                        binding.starImageView.setImageResource(R.drawable.ic_star_empty)
                    } else {
                        favoritesViewModel.addToFavorites(character)
                        binding.starImageView.setImageResource(R.drawable.ic_star)
                    }
                }
            }
        }

        binding.backButton.setOnClickListener {
            // Navega de volta para o HomeFragment
            findNavController().navigateUp()
        }

        characterViewModel.fetchCharacterData(args.characterId)
    }

    private fun updateStarIcon(character: Character) {
        val starResId = if (favoritesViewModel.isFavorite(character)) {
            R.drawable.ic_star
        } else {
            R.drawable.ic_star_empty
        }
        binding.starImageView.setImageResource(starResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}