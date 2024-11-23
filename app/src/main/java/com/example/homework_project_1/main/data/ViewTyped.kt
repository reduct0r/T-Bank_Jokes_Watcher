package com.example.homework_project_1.main.data

import android.net.Uri
import androidx.annotation.IdRes
import com.example.homework_project_1.main.data.models.Flags
import kotlinx.serialization.Contextual
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
    data class JokeUIModel(
        var id: Int,
        @IdRes var avatar: Int?,
        val avatarUri: Uri? = null,
        val category: String,
        val question: String,
        val answer: String,
        var isFavorite: Boolean = false,
        val source: JokeSource
    ) : ViewTyped

    // Класс заголовка
    data class Header(
        val title: String
    ) : ViewTyped

    data class Loading(
        val title: String
    ) : ViewTyped
}

@Serializable
data class Joke(
    var id: Int,
    @IdRes var avatar: Int?,
    @Contextual val avatarUri: Uri? = null,
    val category: String,
    val question: String,
    val answer: String,

    // Поля из запроса
    val type: String,
    val flags: Flags,
    val safe: Boolean,
    val lang: String
)

enum class JokeSource {
    USER,
    NETWORK
}

fun convertToUiModel(dataList: List<Joke>, isFavorite: Boolean): List<ViewTyped.JokeUIModel> {
    return dataList.map { data ->
        ViewTyped.JokeUIModel(
            id = data.id,
            avatar = data.avatar,
            avatarUri = data.avatarUri,
            category = data.category,
            question = data.question,
            answer = data.answer,
            isFavorite = isFavorite,
            source = JokeSource.USER
        )
    }
}
