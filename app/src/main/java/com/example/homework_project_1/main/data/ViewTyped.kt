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

// Класс шутки
data class Joke(
    var id: Int,
    @IdRes var avatar: Int?,
    val avatarUri: Uri? = null,
    val category: String,
    val question: String,
    val answer: String,
)

// Интерфейс для отображения элементов списка
sealed interface ViewTyped {
    // Класс шутки для отображения
    data class JokeUIModel(
        var id: Int,
        @IdRes var avatar: Int?,
        val avatarUri: Uri? = null,
        val category: String,
        val question: String,
        val answer: String,
        val isFavorite: Boolean = false,
    ) : ViewTyped


    // Класс заголовка
    data class Header(
        val title: String
    ) : ViewTyped
}

// Функция-мэппер для преобразования данных в UI модель
fun Joke.toUiJokeModel(isFavorite: Boolean = false): ViewTyped.JokeUIModel {
    return ViewTyped.JokeUIModel(
        id = this.id,
        avatar = this.avatar,
        avatarUri = this.avatarUri,
        category = this.category,
        question = this.question,
        answer = this.answer,
        isFavorite = isFavorite
    )
}
