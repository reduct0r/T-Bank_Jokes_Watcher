package com.example.homework_project_1.main.data.model

import androidx.annotation.IdRes
import androidx.core.net.toUri
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.ViewTyped
import kotlinx.serialization.Serializable

@Serializable
data class JokeDTO(
    var id: Int,
    @IdRes var avatar: Int?,
    val avatarUri: String? = null, // C Uri? на String? для сериализации
    val category: String,
    val question: String,
    val answer: String,
    var source: JokeSource,

    // Доп поля из запроса
    val flags: Flags,
    val lang: String
) {

    companion object {
        fun JokeDTO.convertToUIModel(isFavorite: Boolean): ViewTyped.JokeUIModel {
            return ViewTyped.JokeUIModel(
                id = id,
                category = category,
                question = question,
                answer = answer,
                isFavorite = isFavorite,
                source = source,
                avatar = avatar,
                avatarUri = avatarUri?.toUri(),
            )
        }

        fun List<JokeDTO>.convertToUIModel(isFavorite: Boolean): List<ViewTyped.JokeUIModel> {
            return this.map { joke ->
                ViewTyped.JokeUIModel(
                    id = joke.id,
                    category = joke.category,
                    question = joke.question,
                    answer = joke.answer,
                    isFavorite = isFavorite,
                    source = joke.source,
                    avatar = joke.avatar,
                    avatarUri = joke.avatarUri?.toUri()
                )
            }
        }

        private fun JokeDTO.toEntity(safe: Boolean, type: String) = JokeApiEntity(
            id = id,
            category = category,
            setup = question,
            delivery = answer,
            lang = lang,
            flags = flags,
            safe = safe,
            type = type
        )
    }

}


