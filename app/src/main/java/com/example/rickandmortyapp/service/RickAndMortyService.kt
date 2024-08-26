package com.example.rickandmortyapp.service

import com.example.rickandmortyapp.data.model.CharacterDTO
import com.example.rickandmortyapp.data.model.CharacterResponseDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyService {

    @GET("character")
    suspend fun getAllCharacters(): CharacterResponseDTO
}


