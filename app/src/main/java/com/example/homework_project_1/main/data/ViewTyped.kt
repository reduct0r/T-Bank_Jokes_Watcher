package com.example.homework_project_1.main.data

import androidx.annotation.IdRes

// Интерфейс для отображения элементов списка
sealed interface ViewTyped {
    // Класс шутки
    data class Joke(
        val id: Int,
        @IdRes var avatar: Int?,
        val category: String,
        val question: String,
        val answer: String,
    ) : ViewTyped

    // Класс заголовка
    data class Header(
        val title: String
    ) : ViewTyped
}
