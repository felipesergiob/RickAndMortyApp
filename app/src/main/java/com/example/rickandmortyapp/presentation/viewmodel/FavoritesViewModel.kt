package com.example.rickandmortyapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rickandmortyapp.domain.model.Character
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val _favorites = MutableLiveData<List<Character>>(loadFavorites())
    val favorites: LiveData<List<Character>> get() = _favorites

    fun addToFavorites(character: Character) {
        val updatedList = _favorites.value?.toMutableList()?.apply { add(character) } ?: mutableListOf(character)
        _favorites.value = updatedList
        saveFavorites(updatedList)
        logFavorites("Added to favorites", character)
    }

    fun removeFromFavorites(character: Character) {
        val updatedList = _favorites.value?.toMutableList()?.apply { remove(character) } ?: mutableListOf()
        _favorites.value = updatedList
        saveFavorites(updatedList)
        logFavorites("Removed from favorites", character)
    }

    fun isFavorite(character: Character): Boolean {
        val isFav = _favorites.value?.any { it.id == character.id } == true
        logFavorites("Checking if is favorite: $isFav", character)
        return isFav
    }

    private fun saveFavorites(favoritesList: List<Character>) {
        val editor = sharedPreferences.edit()
        val jsonString = Gson().toJson(favoritesList)
        editor.putString("favorites_list", jsonString)
        editor.apply()
    }

    fun loadFavorites(): List<Character> {
        val jsonString = sharedPreferences.getString("favorites_list", null) ?: return emptyList()
        val type = object : TypeToken<List<Character>>() {}.type
        Log.i("FavoritesViewModel", "teste")
        return Gson().fromJson(jsonString, type)
    }

    private fun logFavorites(action: String, character: Character) {
        Log.d("FavoritesViewModel", "$action: ${character.name} (ID: ${character.id})")
        Log.d("FavoritesViewModel", "Current favorites: ${_favorites.value?.map { it.name }}")
    }
}