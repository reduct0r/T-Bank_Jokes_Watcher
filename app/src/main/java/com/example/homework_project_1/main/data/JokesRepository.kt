package com.example.homework_project_1.main.data

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.delay

// Хранилище шуток
object JokesRepository {
    private val jokesList = mutableListOf<ViewTyped.Joke>()

    fun initialize(context: Context) {
        parseJSON(context)
    }

    private fun parseJSON(context: Context) {
        val jokesData = JsonReader.readJokesFromAsset(context)
        jokesData?.categories?.forEach { category ->
            category.jokes.forEach { jokeDto ->
                val avatarResId = if (jokeDto.avatar != null) {
                    getAvatarResourceId(context, jokeDto.avatar) // Если аватарка указана в JSON, то получить ее ресурс
                } else {
                    null
                }

                jokesList.add(
                    ViewTyped.Joke(
                        id = 0,
                        avatar = avatarResId,
                        category = category.name,
                        question = jokeDto.question,
                        answer = jokeDto.answer
                    )
                )
            }
        }
    }

    // Получение ресурса аватарки по имени
    @SuppressLint("DiscouragedApi")
    private fun getAvatarResourceId(context: Context, avatarName: String?): Int? {
        return avatarName?.let {
            val resId = context.resources.getIdentifier(it, "drawable", context.packageName)
            if (resId != 0) resId else null
        }
    }

    suspend fun getJokes(): List<ViewTyped.Joke> {
        Toast.makeText(null, "Test Delay: Jokes are loading", Toast.LENGTH_SHORT).show()
        delay(2000) // Задержка
        return jokesList
    }

    suspend fun addNewJoke(joke: ViewTyped.Joke) {
        Toast.makeText(null, "Test Delay: Joke is adding", Toast.LENGTH_SHORT).show()
        delay(500) // Задержка
        jokesList.add(joke)
    }
}