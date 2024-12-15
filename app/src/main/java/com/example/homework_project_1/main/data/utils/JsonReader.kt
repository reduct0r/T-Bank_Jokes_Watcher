package com.example.homework_project_1.main.data.utils

import android.annotation.SuppressLint
import android.content.Context
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.model.Flags
import com.example.homework_project_1.main.data.model.JokeDTO
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

// Класс для чтения данных из JSON-файла
class JsonReader {
    private val defaultJokesList = mutableListOf<JokeDTO>()
    private val userJokesList = mutableListOf<JokeDTO>()
    private val categories = mutableSetOf<String>()

    private fun readJokesFromAsset(context: Context, fileName: String = "jokes.json"): JokesData? {
        return try {
            val jsonString = context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<JokesData>(jsonString)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }

    fun parseJSON(context: Context) {
        val jokesData = readJokesFromAsset(context)
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
                        avatarByteArr = null,
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
}
@Serializable
data class JokesData(
    val categories: List<Category>
)

@Serializable
data class Category(
    val name: String,
    val jokes: List<JokeJSONDTO>
)

@Serializable
data class JokeJSONDTO(
    val question: String,
    val answer: String,
    val avatar: String?
)
