package com.example.rickandmortyapp.domain.usecase

import com.example.rickandmortyapp.data.repository.RemoteDataRepository
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.Location
import com.example.rickandmortyapp.domain.model.Origin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCharactersUseCase(
    private val repository: RemoteDataRepository
) {
    suspend operator fun invoke(): Flow<List<Character>> = flow {
        val characters = repository.getCharacters().results.map { dto ->
            Character(
                id = dto.id,
                name = dto.name,
                status = dto.status,
                species = dto.species,
                type = dto.type,
                gender = dto.gender,
                origin = dto.origin.let { Origin(it.name, it.url) },
                location = dto.location.let { Location(it.name, it.url) },
                image = dto.image,
                episode = dto.episode,
                url = dto.url,
                created = dto.created
            )
        }
        emit(characters)
    }
}

