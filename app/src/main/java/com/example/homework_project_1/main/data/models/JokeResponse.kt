package com.example.homework_project_1.main.data.models

import com.example.homework_project_1.main.data.Joke
import kotlinx.serialization.Serializable


@Serializable
data class JokeResponse(
    val error: Boolean,
    val amount: Int,
    val jokes: List<Joke>
)

@Serializable
data class Flags(
    val nsfw: Boolean,
    val religious: Boolean,
    val political: Boolean,
    val racist: Boolean,
    val sexist: Boolean,
    val explicit: Boolean
)