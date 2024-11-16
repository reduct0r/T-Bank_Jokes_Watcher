package com.example.homework_project_1.main.data.uiModel

import android.net.Uri
import com.example.homework_project_1.main.data.ViewTyped

data class JokeUIModel(
    val id: Int,
    val avatar: Int?,
    val avatarUri: Uri?,
    val category: String,
    val question: String,
    val answer: String,
    val isFavorite: Boolean // Новое поле для отображения в UI
) {
    // Функция конвертации из базового класса в UI модель
    companion object {
        fun from(jokeUIModel: ViewTyped.JokeUIModel): JokeUIModel {
            return JokeUIModel(
                id = jokeUIModel.id,
                avatar = jokeUIModel.avatar,
                avatarUri = jokeUIModel.avatarUri,
                category = jokeUIModel.category,
                question = jokeUIModel.question,
                answer = jokeUIModel.answer,
                isFavorite = false
            )
        }
    }
}