package com.example.homework_project_1.main.data

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.delay

object JokesRepository {
    private val jokesList = mutableListOf<ViewTyped.JokeUIModel>()
    private val categories = mutableSetOf<String>()

    fun parseJSON(context: Context) {
        val jokesData = JsonReader.readJokesFromAsset(context)
        jokesData?.categories?.forEach { category ->
            categories.add(category.name)
            category.jokes.forEach { jokeDto ->
                val avatarResId = if (jokeDto.avatar != null) {
                    getAvatarResourceId(context, jokeDto.avatar)
                } else {
                    null
                }

                jokesList.add(
                    ViewTyped.JokeUIModel(
                        id = 0,
                        avatar = avatarResId,
                        category = category.name,
                        question = jokeDto.question,
                        answer = jokeDto.answer,
                    )
                )
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun getAvatarResourceId(context: Context, avatarName: String?): Int? {
        return avatarName?.let {
            val resId = context.resources.getIdentifier(it, "drawable", context.packageName)
            if (resId != 0) resId else null
        }
    }

    suspend fun getJokes(): List<ViewTyped.JokeUIModel> {
        delay(2000)
        return jokesList
    }

    suspend fun addNewJoke(jokeUIModel: ViewTyped.JokeUIModel) {
        delay(500)
        jokesList.add(jokeUIModel)
        categories.add(jokeUIModel.category) // Добавляем категорию, если ее нет
    }

    fun getCategories(): List<String> {
        return categories.toList().sorted()
    }

    fun addNewCategory(newCategory: String) {
        categories.add(newCategory)
    }
}