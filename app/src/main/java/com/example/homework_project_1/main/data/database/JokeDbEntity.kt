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
    val id: Int = 0,

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
    val avatar: ByteArray?
) {
    fun toDto(flags: Flags): JokeDTO {
        return JokeDTO(
            id = id,
            avatar = null,
            avatarUri = null,
            category = category,
            question = question,
            answer = answer,
            source = JokeSource.DATABASE,
            flags = flags,
            lang = "en"
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
        if (avatar != null) {
            if (other.avatar == null) return false
            if (!avatar.contentEquals(other.avatar)) return false
        } else if (other.avatar != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + category.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + answer.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + (avatar?.contentHashCode() ?: 0)
        return result
    }
}
