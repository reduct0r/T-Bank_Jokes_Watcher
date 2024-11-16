package com.example.homework_project_1.main.data

import android.net.Uri
import androidx.annotation.IdRes
import kotlinx.serialization.Serializable

@Serializable
data class JokesData(
    val categories: List<Category>
)

@Serializable
data class Category(
    val name: String,
    val jokes: List<JokeDTO>
)

@Serializable
data class JokeDTO(
    val question: String,
    val answer: String,
    val avatar: String?
)

// Интерфейс для отображения элементов списка
sealed interface ViewTyped {
    // Класс шутки
    data class Joke(
        var id: Int,
        @IdRes var avatar: Int?,
        val avatarUri: Uri? = null,
        val category: String,
        val question: String,
        val answer: String,
    ) : ViewTyped


    // Класс заголовка
    data class Header(
        val title: String
    ) : ViewTyped
}
