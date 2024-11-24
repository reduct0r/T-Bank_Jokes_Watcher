package com.example.homework_project_1.main.data.model

import android.net.Uri
import androidx.annotation.IdRes
import com.example.homework_project_1.main.data.JokeSource
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class JokeDTO(
    var id: Int,
    @IdRes var avatar: Int?,
    val avatarUri: String? = null, // C Uri? на String? для сериализации
    val category: String,
    val question: String,
    val answer: String,
    val source: JokeSource,

    // Доп поля из запроса
    val type: String,
    val flags: FlagsDTO,
    val safe: Boolean,
    val lang: String
)
