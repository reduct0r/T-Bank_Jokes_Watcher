package com.example.homework_project_1.main.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface JokeDAO {
    @Insert
    suspend fun insert(joke: JokeEntity)
    @Update
    suspend fun update(joke: JokeEntity)
    @Delete
    suspend fun delete(joke: JokeEntity)
    @Query("SELECT * FROM jokes")
    suspend fun getAllUsers(): List<JokeEntity>
}