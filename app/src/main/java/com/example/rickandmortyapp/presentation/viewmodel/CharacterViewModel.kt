package com.example.rickandmortyapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CharacterViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _characterData = MutableLiveData<Character>()
    val characterData: LiveData<Character> get() = _characterData

    private val _charactersList = MutableLiveData<List<Character>>()
    val charactersList: LiveData<List<Character>> get() = _charactersList

    fun fetchCharacterData(characterId: Int) {
        viewModelScope.launch {
            getCharactersUseCase().collect { characters ->
                val character = characters.firstOrNull { it.id == characterId }
                character?.let {
                    _characterData.postValue(it)
                }
            }
        }
    }

    fun fetchAllCharacters() {
        viewModelScope.launch {
            getCharactersUseCase().collect { characters ->
                _charactersList.postValue(characters)
            }
        }
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date: Date? = inputFormat.parse(dateString)
        return if (date != null) outputFormat.format(date) else "Data inválida"
    }
}
