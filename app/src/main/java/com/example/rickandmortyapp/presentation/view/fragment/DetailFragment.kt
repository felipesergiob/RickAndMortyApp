package com.example.rickandmortyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.FragmentDetailBinding
import com.example.rickandmortyapp.viewmodel.CharacterViewModel
import com.example.rickandmortyapp.viewmodel.FavoritesViewModel

class DetailFragment : Fragment() {

    private val characterViewModel: CharacterViewModel by activityViewModels()
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
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
                    gender: ${character.gender}
                    species: ${character.species}
                    status: ${character.status}
                    created: ${character.created}
                """.trimIndent()

                Glide.with(this)
                    .load(character.image)
                    .into(binding.characterImageView)

                updateStarIcon(it)

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
            findNavController().navigateUp()
        }

        characterViewModel.fetchCharacterData(args.characterId)
    }

    private fun updateStarIcon(character: com.example.rickandmortyapp.domain.model.Character) {
        val starResId = if (favoritesViewModel.isFavorite(character)) {
            R.drawable.ic_star // Imagem verde
        } else {
            R.drawable.ic_star_empty // Imagem vazia
        }
        binding.starImageView.setImageResource(starResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
