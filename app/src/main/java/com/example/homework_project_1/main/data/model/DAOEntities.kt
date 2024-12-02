package com.example.homework_project_1.main.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.homework_project_1.main.data.JokeSource
import kotlinx.serialization.Serializable

@Entity(tableName = "jokes")
@Serializable
data class JokeEntity(
    @PrimaryKey
    val id: Int,
    val category: String,
    val type: String,
    val setup: String? = null,
    val delivery: String? = null,
    @Embedded
    val flags: FlagsEntity,
    val safe: Boolean,
    val lang: String
)

@Serializable
data class FlagsEntity(
    val nsfw: Boolean,
    val religious: Boolean,
    val political: Boolean,
    val racist: Boolean,
    val sexist: Boolean,
    val explicit: Boolean
)

fun JokeEntity.toDto(flags: FlagsEntity) = JokeDTO(
    id = id,
    category = category,
    question = setup.toString(),
    answer = delivery.toString(),
    lang = lang,
    flags = flags.toDto(),
    avatar = null,
    avatarUri = null,
    source = JokeSource.DEFAULT
)

fun FlagsEntity.toDto() = FlagsDTO(
    nsfw = nsfw,
    religious = religious,
    political = political,
    racist = racist,
    sexist = sexist,
    explicit = explicit
)

fun JokeDTO.toEntity(safe: Boolean, type: String) = JokeEntity(
    id = id,
    category = category,
    setup = question,
    delivery = answer,
    lang = lang,
    flags = flags.toEntity(id),
    safe = safe,
    type = type
)

fun FlagsDTO.toEntity(jokeId: Int) = FlagsEntity(
    nsfw = nsfw,
    religious = religious,
    political = political,
    racist = racist,
    sexist = sexist,
    explicit = explicit
)