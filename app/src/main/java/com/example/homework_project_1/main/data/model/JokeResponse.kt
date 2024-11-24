package com.example.homework_project_1.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class JokeResponse(
    val error: Boolean,
    val amount: Int,
    val jokes: List<JokeEntity>
)