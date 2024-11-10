package com.example.homework_project_1.main.data

import android.content.Context
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