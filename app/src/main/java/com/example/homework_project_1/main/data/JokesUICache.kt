package com.example.homework_project_1.main.data

object JokesCache {
    private val cache = mutableMapOf<Int, ViewTyped.JokeUIModel>()
    private val allJokesCache = mutableListOf<ViewTyped.JokeUIModel>()

    fun cacheJoke(position: Int, joke: ViewTyped.JokeUIModel) {
        cache[position] = joke
    }

    fun getJoke(position: Int): ViewTyped.JokeUIModel? {
        return cache[position]
    }

    fun cacheAllJokes(jokes: List<ViewTyped.JokeUIModel>) {
        allJokesCache.clear()
        allJokesCache.addAll(jokes)
    }

    fun getAllJokes(): List<ViewTyped.JokeUIModel> {
        return allJokesCache.toList()
    }

    fun clear() {
        cache.clear()
        allJokesCache.clear()
    }
}