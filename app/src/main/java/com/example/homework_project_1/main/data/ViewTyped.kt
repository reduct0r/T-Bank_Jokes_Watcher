package com.example.homework_project_1.main.data

import android.net.Uri
import androidx.annotation.IdRes
import androidx.core.net.toUri
import com.example.homework_project_1.main.data.model.JokeDTO
import kotlinx.serialization.Serializable
import com.example.homework_project_1.main.data.model.JokeEntity
import kotlinx.serialization.Contextual

// UI-Интерфейс для отображения элементов списка
@Serializable
sealed interface ViewTyped {
    // Класс шутки
    data class JokeUIModel(
        var id: Int,
        @IdRes var avatar: Int?,
        val avatarUri: Uri? = null,
        val category: String,
        val question: String,
        val answer: String,
        var isFavorite: Boolean = false,
        val source: JokeSource

    ) : ViewTyped, java.io.Serializable

    // Класс заголовка
    data class Header(
        val title: String
    ) : ViewTyped

    // Класс загрузки
    data object Loading : ViewTyped
}

enum class JokeSource {
    DEFAULT,
    USER,
    NETWORK
}

