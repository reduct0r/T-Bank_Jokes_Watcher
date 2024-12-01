package com.example.homework_project_1.main.data.database

import androidx.room.TypeConverter
import com.example.homework_project_1.main.data.model.Flags
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromFlags(flags: Flags): String {
        return Json.encodeToString(Flags.serializer(), flags) // Сериализация
    }

    @TypeConverter
    fun toFlags(flagsJson: String): Flags {
        return Json.decodeFromString(Flags.serializer(), flagsJson) // Десериализация
    }
}