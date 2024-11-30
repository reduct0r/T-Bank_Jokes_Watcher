package com.example.homework_project_1.main.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface JokeDAO {
    @Insert
    suspend fun insert(joke: JokeDbEntity)

    @Update
    suspend fun update(joke: JokeDbEntity)

    @Delete
    suspend fun delete(joke: JokeDbEntity)

    @Query("SELECT * FROM jokes")
    suspend fun getAllJokes(): List<JokeDbEntity>
}