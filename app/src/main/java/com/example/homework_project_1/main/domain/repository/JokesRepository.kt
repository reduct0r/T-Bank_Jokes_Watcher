package com.example.homework_project_1.main.domain.repository

import com.example.homework_project_1.main.data.model.JokeDTO

interface JokesRepository : Repository {
    fun addNewCategory(newCategory: String)
    suspend fun getCategories(): List<String>
    suspend fun getFavoriteJokes(): List<JokeDTO>
    suspend fun changeFavouriteStatus(jokeId: Int, isFavourite: Boolean)
}