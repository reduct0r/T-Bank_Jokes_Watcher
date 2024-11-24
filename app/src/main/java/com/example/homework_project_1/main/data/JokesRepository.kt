package com.example.homework_project_1.main.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.homework_project_1.main.data.models.Flags
import com.example.homework_project_1.main.data.models.JokeResponse
import com.example.homework_project_1.main.data.network.NetworkModule.client
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID

object JokesRepository {
    private val defaultJokesList = mutableListOf<Joke>()
    private val userJokesList = mutableListOf<Joke>()
    private val networkJokesList = mutableListOf<Joke>()
    private val categories = mutableSetOf<String>()

    private var currentPage = 1
    private val jokesPerPage = 25
    private var isLoading = false

    private val _userJokesLiveData = MutableLiveData<List<Joke>>()
    val userJokesLiveData: LiveData<List<Joke>> get() = _userJokesLiveData

    private val _jokesLiveData = MutableLiveData<List<ViewTyped>>()
    val jokesLiveData: LiveData<List<ViewTyped>> get() = _jokesLiveData

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
                defaultJokesList.add(
                    Joke(
                        id = UUID.randomUUID().hashCode(),
                        avatar = avatarResId,
                        category = category.name,
                        question = jokeDto.question,
                        answer = jokeDto.answer,
                        type = "single",
                        flags = Flags(false, false, false, false, false, false),
                        safe = true,
                        lang = "en",
                        source = JokeSource.DEFAULT
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

    suspend fun getJokes(): List<Joke> {
        delay(500)
        return defaultJokesList + userJokesList
    }

    suspend fun addNewJoke(joke: Joke) {
        delay(5000)
        userJokesList.add(joke)
        _userJokesLiveData.postValue(userJokesList.toList())

        if (joke.category !in categories ) {
            categories.add(joke.category)
        }
        JokesGenerator.addToSelectedJokes(joke, userJokesList.size + defaultJokesList.size - 1)
    }

    suspend fun fetchJokesFromNetwork() {
        if (isLoading) return
        isLoading = true
        try {
            val response: JokeResponse = client.get("https://v2.jokeapi.dev/joke/Any") {
                parameter("blacklistFlags", "nsfw,religious,political,racist,sexist,explicit")
                parameter("type", "twopart")
                parameter("amount", jokesPerPage)
            }.body()

            if (!response.error) {
                networkJokesList.addAll(response.jokes)
                updateJokesLiveData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Обработка ошибок (можно добавить LiveData для ошибок)
        } finally {
            isLoading = false
        }
    }

    suspend fun loadMoreJokes() {
        withContext(Dispatchers.IO) {
            fetchJokesFromNetwork()
        }
    }

    private fun updateJokesLiveData() {
        val combinedList = mutableListOf<ViewTyped>()

        // Добавляем пользовательские шутки
        userJokesList.forEach { userJoke ->
            combinedList.add(
                ViewTyped.JokeUIModel(
                    id = userJoke.id,
                    avatar = null,
                    avatarUri = null,
                    category = userJoke.category,
                    question = userJoke.question,
                    answer = userJoke.answer,
                    isFavorite = false,
                    source = JokeSource.USER
                )
            )
        }

        // Добавляем сетевые шутки
        networkJokesList.forEach { networkJoke ->
            combinedList.add(
                ViewTyped.JokeUIModel(
                    id = networkJoke.id,
                    avatar = null,
                    avatarUri = null,
                    category = networkJoke.category,
                    question = networkJoke.question,
                    answer = networkJoke.answer,
                    isFavorite = false,
                    source = JokeSource.NETWORK
                )
            )
        }

        _jokesLiveData.postValue(combinedList)
    }

    fun getUserJokes(): LiveData<List<Joke>> {
        return _userJokesLiveData
    }

    fun getCategories(): List<String> {
        return categories.toList().sorted()
    }

    fun addNewCategory(newCategory: String) {
        categories.add(newCategory)
    }
}
