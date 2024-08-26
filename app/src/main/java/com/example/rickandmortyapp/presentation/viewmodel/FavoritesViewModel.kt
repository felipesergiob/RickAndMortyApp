package com.example.rickandmortyapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rickandmortyapp.domain.model.Character // Importando a classe correta
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
    }

    fun removeFromFavorites(character: Character) {
        val updatedList = _favorites.value?.toMutableList()?.apply { remove(character) } ?: mutableListOf()
        _favorites.value = updatedList
        saveFavorites(updatedList)
    }

    fun isFavorite(character: Character): Boolean {
        return _favorites.value?.contains(character) == true
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
        return Gson().fromJson(jsonString, type)
    }
}
