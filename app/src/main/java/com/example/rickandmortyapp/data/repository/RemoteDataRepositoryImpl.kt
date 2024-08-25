package com.example.rickandmortyapp.data.repository

import com.example.rickandmortyapp.service.RickAndMortyService

class RemoteDataRepositoryImpl(
    private val service: RickAndMortyService
) : RemoteDataRepository {

    override suspend fun getCharacters() = service.getAllCharacters()
}
