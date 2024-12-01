package com.example.homework_project_1.main.data.model

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.annotation.IdRes
import androidx.core.net.toUri
import com.example.homework_project_1.main.App
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.ViewTyped
import com.example.homework_project_1.main.data.database.JokeDbEntity
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream
import java.io.InputStream

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
    @SuppressLint("Recycle")
    fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val byteBuffer = ByteArrayOutputStream()
            val buffer = ByteArray(10240)
            var len: Int
            while (inputStream?.read(buffer).also { len = it ?: -1 } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
            byteBuffer.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        fun JokeDTO.convertToUIModel(isFavorite: Boolean): ViewTyped.JokeUIModel {
            return ViewTyped.JokeUIModel(
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

        fun JokeDTO.toDbEntity(context: Context): JokeDbEntity {
            val avatarBytes: ByteArray? = avatarUri?.toUri()?.let { uri ->
                uriToByteArray(context, uri)
            }

            return JokeDbEntity(
                id = null,
                category = category,
                question = question,
                answer = answer,
                flags = flags,
                avatar = avatarBytes,
                source = source.toString()
            )
        }
    }

}


