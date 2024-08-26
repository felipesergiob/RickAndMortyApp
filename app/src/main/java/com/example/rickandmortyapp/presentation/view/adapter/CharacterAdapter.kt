package com.example.rickandmortyapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.ItemCharacterBinding
import com.example.rickandmortyapp.databinding.ItemFavoriteCharacterBinding
import com.example.rickandmortyapp.viewmodel.FavoritesViewModel
import com.example.rickandmortyapp.domain.model.Character

class CharacterAdapter(
    private val characters: List<Character>,
    private val favoritesViewModel: FavoritesViewModel,
    private val onItemClick: ((Character) -> Unit)? = null,
    private val allowFavoriteToggle: Boolean = true,
    private val isFavoriteView: Boolean = false
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = if (isFavoriteView) {
            ItemFavoriteCharacterBinding.inflate(layoutInflater, parent, false)
        } else {
            ItemCharacterBinding.inflate(layoutInflater, parent, false)
        }
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount() = characters.size

    inner class CharacterViewHolder(
        private val binding: Any
    ) : RecyclerView.ViewHolder(
        if (binding is ItemCharacterBinding) binding.root else (binding as ItemFavoriteCharacterBinding).root
    ) {
        fun bind(character: Character) {
            if (binding is ItemCharacterBinding) {
                binding.characterName.text = character.name
                binding.characterSpecies.text = character.species

                Glide.with(binding.root)
                    .load(character.image)
                    .into(binding.characterImage)

                val isFavorite = favoritesViewModel.isFavorite(character)
                val starResId = if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_empty
                binding.starIcon.setImageResource(starResId)

                binding.root.setOnClickListener {
                    onItemClick?.invoke(character)
                }

                if (allowFavoriteToggle) {
                    binding.starIcon.setOnClickListener {
                        if (isFavorite) {
                            favoritesViewModel.removeFromFavorites(character)
                            binding.starIcon.setImageResource(R.drawable.ic_star_empty)
                        } else {
                            favoritesViewModel.addToFavorites(character)
                            binding.starIcon.setImageResource(R.drawable.ic_star)
                        }
                    }
                }
            } else if (binding is ItemFavoriteCharacterBinding) {
                binding.favoriteCharacterName.text = character.name
                binding.favoriteCharacterSpecies.text = character.species

                Glide.with(binding.root)
                    .load(character.image)
                    .into(binding.favoriteCharacterImage)

                binding.root.setOnClickListener {
                    onItemClick?.invoke(character)
                }

                binding.favoriteStarIcon.setOnClickListener {
                    favoritesViewModel.removeFromFavorites(character)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }
    }
}
