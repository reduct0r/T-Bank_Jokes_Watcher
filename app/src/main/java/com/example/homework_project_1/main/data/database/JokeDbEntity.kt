package com.example.homework_project_1.main.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.homework_project_1.main.data.JokeSource
import com.example.homework_project_1.main.data.model.Flags
import com.example.homework_project_1.main.data.model.JokeDTO

@Entity(tableName = "jokes")
data class JokeDbEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "question")
    val question: String,
    @ColumnInfo(name = "answer")
    val answer: String,
    @ColumnInfo(name = "source")
    val source: String,
    @ColumnInfo(name = "flags")
    val flags: Flags,
    @ColumnInfo(name = "avatar")
    val avatarByteArr: ByteArray?,
    @ColumnInfo(name = "isShown")
    var isShown: Boolean = false,
    @ColumnInfo(name = "createdAt")
    var createdAt: Long,
    @ColumnInfo(name = "isFavourite")
    var isFavourite: Boolean = false
) {

    fun toDto(): JokeDTO {
        return JokeDTO(
            id = id ?: 0,
            avatar = null,
            avatarByteArr = avatarByteArr,
            category = category,
            question = question,
            answer = answer,
            source = if (source == "USER"){JokeSource.USER} else {JokeSource.DATABASE},
            flags = flags,
            lang = "en",
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JokeDbEntity

        if (id != other.id) return false
        if (category != other.category) return false
        if (question != other.question) return false
        if (answer != other.answer) return false
        if (source != other.source) return false
        if (flags != other.flags) return false
        if (avatarByteArr != null) {
            if (other.avatarByteArr == null) return false
            if (!avatarByteArr.contentEquals(other.avatarByteArr)) return false
        } else if (other.avatarByteArr != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + category.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + answer.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + flags.hashCode()
        result = 31 * result + (avatarByteArr?.contentHashCode() ?: 0)
        return result
    }
}


