package com.example.homework_project_1.main.data.model

import androidx.annotation.IdRes
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.presentation.utils.ViewTyped
import com.example.homework_project_1.main.data.database.JokeCacheEntity
import com.example.homework_project_1.main.data.database.JokeDbEntity
import kotlinx.serialization.Serializable

@Serializable
data class JokeDTO(
    var id: Int,
    @IdRes var avatar: Int?,
    val avatarByteArr: ByteArray?,
    val category: String,
    val question: String,
    val answer: String,
    var source: JokeSource,
    var isFavorite : Boolean,

    // Доп поля из запроса
    val flags: Flags,
    val lang: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JokeDTO

        if (id != other.id) return false
        if (avatar != other.avatar) return false
        if (avatarByteArr != null) {
            if (other.avatarByteArr == null) return false
            if (!avatarByteArr.contentEquals(other.avatarByteArr)) return false
        } else if (other.avatarByteArr != null) return false
        if (category != other.category) return false
        if (question != other.question) return false
        if (answer != other.answer) return false
        if (source != other.source) return false
        if (flags != other.flags) return false
        if (lang != other.lang) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (avatar ?: 0)
        result = 31 * result + (avatarByteArr?.contentHashCode() ?: 0)
        result = 31 * result + category.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + answer.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + flags.hashCode()
        result = 31 * result + lang.hashCode()
        return result
    }

    companion object {
        fun JokeDTO.convertToUIModel(): ViewTyped.JokeUIModel {
            return ViewTyped.JokeUIModel(
                id = id,
                category = category,
                question = question,
                answer = answer,
                isFavorite = isFavorite,
                source = source,
                avatar = avatar,
                avatarByteArr = avatarByteArr,
            )
        }

        fun List<JokeDTO>.convertToUIModel(): List<ViewTyped.JokeUIModel> {
            return this.map { joke ->
                ViewTyped.JokeUIModel(
                    id = joke.id,
                    category = joke.category,
                    question = joke.question,
                    answer = joke.answer,
                    isFavorite = joke.isFavorite,
                    source = joke.source,
                    avatar = joke.avatar,
                    avatarByteArr = joke.avatarByteArr
                )
            }
        }

        fun JokeDTO.toApiEntity(safe: Boolean, type: String) = JokeApiEntity(
            id = id,
            category = category,
            setup = question,
            delivery = answer,
            lang = lang,
            flags = flags,
            safe = safe,
            type = type
        )

        fun JokeDTO.toDbEntity(lastTimestamp: Long = System.currentTimeMillis()): JokeDbEntity {
            return JokeDbEntity(
                id = null,
                category = category,
                question = question,
                answer = answer,
                flags = flags,
                avatarByteArr = avatarByteArr,
                source = source.toString(),
                isShown = false,
                createdAt = lastTimestamp,
                isFavourite = isFavorite,
                avatarUrl = avatar
            )
        }

        fun JokeDTO.toCacheEntity(lastTimestamp: Long = System.currentTimeMillis()): JokeCacheEntity {
            return JokeCacheEntity(
                id = null,
                category = category,
                question = question,
                answer = answer,
                flags = flags,
                avatarByteArr = avatarByteArr,
                source = source.toString(),
                isShown = false,
                createdAt = lastTimestamp,
                isFavourite = isFavorite,
                avatarUrl = avatar
            )
        }

}}


