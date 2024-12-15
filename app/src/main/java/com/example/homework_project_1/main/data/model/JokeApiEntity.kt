package com.example.homework_project_1.main.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.homework_project_1.main.data.JokeSource
import kotlinx.serialization.Serializable

@Entity(tableName = "jokes")
@Serializable
data class JokeApiEntity(
    @PrimaryKey
    val id: Int,
    val category: String,
    val type: String,
    val setup: String? = null,
    val delivery: String? = null,
    @Embedded
    val flags: Flags,
    val safe: Boolean,
    val lang: String
){

   fun toDto(flags: Flags): JokeDTO {
        return JokeDTO(
        id = id,
        category = category,
        question = setup.toString(),
        answer = delivery.toString(),
        lang = lang,
        flags = flags,
        avatar = null,
        avatarByteArr = null,
        source = JokeSource.DEFAULT,
        isFavorite = false
        )
    }
}


