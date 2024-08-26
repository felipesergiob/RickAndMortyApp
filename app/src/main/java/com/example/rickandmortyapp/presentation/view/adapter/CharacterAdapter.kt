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
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_DEFAULT = 1
    private val VIEW_TYPE_FAVORITE = 2

    override fun getItemViewType(position: Int): Int {
        return if (isFavoriteView) VIEW_TYPE_FAVORITE else VIEW_TYPE_DEFAULT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FAVORITE) {
            val binding = ItemFavoriteCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FavoriteViewHolder(binding)
        } else {
            val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DefaultViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val character = characters[position]
        if (holder is FavoriteViewHolder) {
            holder.bind(character)
        } else if (holder is DefaultViewHolder) {
            holder.bind(character)
        }
    }

    override fun getItemCount() = characters.size

    inner class DefaultViewHolder(private val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
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
        }
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoriteCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
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
