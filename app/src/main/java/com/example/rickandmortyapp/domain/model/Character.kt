package com.example.rickandmortyapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: @RawValue Origin,
    val location: @RawValue Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    var created: String,
) : Parcelable

