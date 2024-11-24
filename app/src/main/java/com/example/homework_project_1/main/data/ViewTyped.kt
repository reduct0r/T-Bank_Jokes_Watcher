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
    data class Loading(
        val title: String
    ) : ViewTyped
}

enum class JokeSource {
    DEFAULT,
    USER,
    NETWORK
}

fun convertToUiModel(isFavorite: Boolean, vararg dataList: JokeDTO): List<ViewTyped.JokeUIModel> {
    return dataList.map { data ->
        ViewTyped.JokeUIModel(
            id = data.id,
            category = data.category,
            question = data.question,
            answer = data.answer,
            isFavorite = isFavorite,
            source = data.source,
            avatar = data.avatar,
            avatarUri = data.avatarUri?.toUri(),
        )
    }
}
