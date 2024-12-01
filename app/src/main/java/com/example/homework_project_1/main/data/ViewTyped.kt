package com.example.homework_project_1.main.data

import android.net.Uri
import androidx.annotation.IdRes
import kotlinx.serialization.Serializable

// UI-Интерфейс для отображения элементов списка
@Serializable
sealed interface ViewTyped {
    // Класс шутки
    data class JokeUIModel(
        @IdRes var avatar: Int?,
        val avatarByteArr: ByteArray? = null,
        val category: String,
        val question: String,
        val answer: String,
        var isFavorite: Boolean = false,
        val source: JokeSource

    ) : ViewTyped, java.io.Serializable {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as JokeUIModel

            if (avatar != other.avatar) return false
            if (avatarByteArr != null) {
                if (other.avatarByteArr == null) return false
                if (!avatarByteArr.contentEquals(other.avatarByteArr)) return false
            } else if (other.avatarByteArr != null) return false
            if (category != other.category) return false
            if (question != other.question) return false
            if (answer != other.answer) return false
            if (isFavorite != other.isFavorite) return false
            if (source != other.source) return false

            return true
        }

        override fun hashCode(): Int {
            var result = avatar ?: 0
            result = 31 * result + (avatarByteArr?.contentHashCode() ?: 0)
            result = 31 * result + category.hashCode()
            result = 31 * result + question.hashCode()
            result = 31 * result + answer.hashCode()
            result = 31 * result + isFavorite.hashCode()
            result = 31 * result + source.hashCode()
            return result
        }
    }

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
    NETWORK,
    DATABASE,
    CACHE
}

