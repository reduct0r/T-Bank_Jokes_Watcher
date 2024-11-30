package com.example.homework_project_1.main.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.homework_project_1.main.data.model.Flags
import com.example.homework_project_1.main.data.model.JokeDTO
import kotlinx.coroutines.delay
import java.util.UUID

object JokesRepository {
    private val defaultJokesList = mutableListOf<JokeDTO>()
    private val userJokesList = mutableListOf<JokeDTO>()
    private val categories = mutableSetOf<String>()



    private val _userJokesLiveData = MutableLiveData<List<JokeDTO>>()
    //val userJokesLiveData: LiveData<List<JokeDTO>> get() = _userJokesLiveData

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
                    JokeDTO(
                        id = UUID.randomUUID().hashCode(),
                        avatar = avatarResId,
                        category = category.name,
                        question = jokeDto.question,
                        answer = jokeDto.answer,
                        flags = Flags(
                            nsfw = false,
                            religious = false,
                            political = false,
                            racist = false,
                            sexist = false,
                            explicit = false
                        ),
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

    suspend fun getJokes(): List<JokeDTO> {
        delay(500)
        return defaultJokesList + userJokesList
    }

    suspend fun addNewJoke(joke: JokeDTO) {
        delay(2000)
        userJokesList.add(joke)
        _userJokesLiveData.postValue(userJokesList.toList())

        if (joke.category !in categories ) {
            categories.add(joke.category)
        }
        JokesGenerator.addToSelectedJokes(joke, index = userJokesList.size + defaultJokesList.size - 1)
    }


    fun getUserJokes(): LiveData<List<JokeDTO>> {
        return _userJokesLiveData
    }

    fun getCategories(): List<String> {
        return categories.toList().sorted()
    }

    fun addNewCategory(newCategory: String) {
        categories.add(newCategory)
    }
}
