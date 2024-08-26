package com.example.rickandmortyapp.core.di

import com.example.rickandmortyapp.core.utils.Constants
import com.example.rickandmortyapp.data.repository.RemoteDataRepository
import com.example.rickandmortyapp.data.repository.RemoteDataRepositoryImpl
import com.example.rickandmortyapp.domain.usecase.GetCharactersUseCase
import com.example.rickandmortyapp.service.RickAndMortyService
import com.example.rickandmortyapp.viewmodel.CharacterViewModel
import com.example.rickandmortyapp.viewmodel.FavoritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyService::class.java)
    }

    single<RemoteDataRepository> { RemoteDataRepositoryImpl(get()) }
    factory { GetCharactersUseCase(get()) }

    viewModel { CharacterViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
}
