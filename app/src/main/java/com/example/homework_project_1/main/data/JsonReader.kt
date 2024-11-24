package com.example.homework_project_1.main.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

// Класс для чтения данных из JSON-файла
object JsonReader {
    fun readJokesFromAsset(context: Context, fileName: String = "jokes.json"): JokesData? {
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
