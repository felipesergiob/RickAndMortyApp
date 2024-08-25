package com.example.rickandmortyapp.data.repository

import com.example.rickandmortyapp.data.model.CharacterResponseDTO

interface RemoteDataRepository {
    suspend fun getCharacters(): CharacterResponseDTO
}
