package com.example.homework_project_1.main.data

import androidx.annotation.IdRes;

sealed interface ViewTyped {
    data class Joke(
        val id: Int,
        @IdRes var avatar: Int?,
        val category: String,
        val question: String,
        val answer: String,
    ) : ViewTyped

    data class Header(
        val title: String
    ) : ViewTyped
}
