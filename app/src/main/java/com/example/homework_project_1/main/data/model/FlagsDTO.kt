package com.example.homework_project_1.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FlagsDTO(
    val nsfw: Boolean,
    val religious: Boolean,
    val political: Boolean,
    val racist: Boolean,
    val sexist: Boolean,
    val explicit: Boolean
)