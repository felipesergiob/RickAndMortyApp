package com.example.rickandmortyapp.data.model

data class CharacterResponseDTO(
    val info: InfoDTO,
    val results: List<CharacterDTO>
)

data class InfoDTO(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
