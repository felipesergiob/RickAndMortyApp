package com.example.rickandmortyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.data.model.CharacterDTO
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.Location
import com.example.rickandmortyapp.domain.model.Origin
import com.example.rickandmortyapp.service.RickAndMortyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class CharacterViewModel(
    private val service: RickAndMortyService
) : ViewModel() {

    private val _charactersList = MutableLiveData<List<Character>>()
    val charactersList: LiveData<List<Character>> get() = _charactersList

    fun fetchAllCharacters() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    service.getAllCharacters()
                }
                val characters = response.results.map { characterDTO ->
                    characterDTO.toDomainModel()
                }
                _charactersList.postValue(characters)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun CharacterDTO.toDomainModel(): Character {
        return Character(
            id = this.id,
            name = this.name,
            status = this.status,
            species = this.species,
            type = this.type,
            gender = this.gender,
            origin = Origin(this.origin.name, this.origin.url),
            location = Location(this.location.name, this.location.url),
            image = this.image,
            episode = this.episode,
            url = this.url,
            created = formatDate(this.created)
        )
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date: Date? = inputFormat.parse(dateString)
        return if (date != null) {
            outputFormat.format(date)
        } else {
            "Data inválida"
        }
    }
}
